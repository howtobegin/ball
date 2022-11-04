package com.ball.biz.bet.processor;

import com.alibaba.fastjson.JSON;
import com.ball.base.model.enums.YesOrNo;
import com.ball.base.transaction.TransactionSupport;
import com.ball.base.util.BeanUtil;
import com.ball.base.util.BizAssert;
import com.ball.base.util.IDCreator;
import com.ball.biz.account.entity.TradeConfig;
import com.ball.biz.account.entity.UserAccount;
import com.ball.biz.account.enums.AccountTransactionType;
import com.ball.biz.account.enums.PlayTypeEnum;
import com.ball.biz.account.enums.SportEnum;
import com.ball.biz.account.service.ICurrencyService;
import com.ball.biz.account.service.ITradeConfigService;
import com.ball.biz.account.service.IUserAccountService;
import com.ball.biz.bet.enums.HandicapType;
import com.ball.biz.bet.enums.Sport;
import com.ball.biz.bet.order.OrderHelper;
import com.ball.biz.bet.order.bo.BetBo;
import com.ball.biz.bet.order.bo.OddsData;
import com.ball.biz.bet.processor.assist.OddsAssist;
import com.ball.biz.bet.processor.bo.BetInfo;
import com.ball.biz.bet.processor.bo.OddsCheckInfo;
import com.ball.biz.bet.processor.cache.BetCache;
import com.ball.biz.exception.BizErrCode;
import com.ball.biz.exception.BizException;
import com.ball.biz.match.entity.Odds;
import com.ball.biz.match.entity.Schedules;
import com.ball.biz.match.service.IOddsScoreService;
import com.ball.biz.match.service.IOddsService;
import com.ball.biz.match.service.ISchedulesService;
import com.ball.biz.order.entity.OrderInfo;
import com.ball.biz.order.service.IOrderHistoryService;
import com.ball.biz.order.service.IOrderInfoService;
import com.ball.biz.order.service.IOrderStatService;
import com.ball.biz.order.service.IOrderSummaryService;
import com.ball.biz.user.entity.UserInfo;
import com.ball.biz.user.service.IUserInfoService;
import com.google.common.primitives.Longs;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

/**
 * @author lhl
 * @date 2022/10/22 上午11:02
 */
@Slf4j
public abstract class AbstractBetProcessor implements BetProcessor, InitializingBean {
    @Autowired
    protected IUserInfoService userInfoService;
    @Autowired
    protected IUserAccountService userAccountService;
    @Autowired
    protected TransactionSupport transactionSupport;
    @Autowired
    protected ISchedulesService schedulesService;
    @Autowired
    protected IOddsService oddsService;
    @Autowired
    protected IOddsScoreService oddsScoreService;
    @Autowired
    protected IOrderInfoService orderInfoService;
    @Autowired
    protected IOrderHistoryService orderHistoryService;
    @Autowired
    private ITradeConfigService tradeConfigService;
    @Autowired
    private IOrderStatService orderStatService;
    @Autowired
    private IOrderSummaryService orderSummaryService;
    @Autowired
    private ICurrencyService currencyService;

    /**
     * 赔率允许延迟的时间，默认-1，表示不限制
     * odds，任务是2秒一次
     * oddsScore，任务10秒一次
     */
    @Value("${bet.odds.allow.delay:6}")
    private Long allowDelay;

    /**
     * 下注总控
     */
    @Value("${bet.enable:true}")
    private boolean enable;

    /**
     * 全站最小限制，单位RMB
     */
    @Value("${system.bet.min:50}")
    protected BigDecimal systemBetMin;

    /**
     * 投注
     * @param bo
     * @return
     */
    @Override
    public OrderInfo bet(BetBo bo) {
        log.info("bet start betBo {} enable {} isEnable {}", JSON.toJSON(bo), enable, isEnable());
        BizAssert.isTrue(enable, BizErrCode.BET_ALL_CLOSE);
        BizAssert.isTrue(isEnable(), BizErrCode.BET_THIS_TYPE_CLOSE);

        OrderInfo order;
        try {
            betCheck(bo, true);
            String orderNo = IDCreator.get();
            BigDecimal fee = BigDecimal.ZERO;
            // 构建订单信息，状态待确认
            order = buildOrder(bo, orderNo);
            transactionSupport.execute(()->{
                // 冻结
                userAccountService.freeze(bo.getUserNo(), bo.getBetAmount(), orderNo, fee, AccountTransactionType.TRADE);
                // 保存订单信息
                orderInfoService.save(order);
                // 增加历史
                orderHistoryService.saveLatest(order);
                // 处理订单统计
                orderStatService.newOrderCreate(order);
                orderSummaryService.newOrderCreate(order);
            });
        } finally {
            BetCache.clear();
        }
        log.info("bet end");
        return order;
    }

    @Override
    public void betCheck(BetBo bo, boolean checkUser) {
        log.info("start getOddsCheckInfo");
        OddsCheckInfo checkInfo = getOddsCheckInfo(bo);
        log.info("start checkSchedule");
        // 校验赛事
        checkSchedule(bo, checkInfo.getMatchId());
        log.info("start checkOdds");
        // 校验投注信息，是否存在，关闭等
        checkOdds(checkInfo, bo);
        // 投注选项是否合理
        BizAssert.isTrue(bo.getHandicapType().getBetOptions().contains(bo.getBetOption()), BizErrCode.PARAM_ERROR_DESC, "betOption");
        log.info("start checkUser");
        if (checkUser) {
            // 校验用户状态，余额等
            checkUser(bo, checkInfo.getOddsType());
        }
    }

    protected OddsCheckInfo getOddsCheckInfo(BetBo bo) {
        String bizNo = bo.getBizNo();
        Odds odds = oddsService.queryByBizNo(bizNo);
        BizAssert.notNull(odds, BizErrCode.DATA_NOT_EXISTS);

        return OddsCheckInfo.builder()
                .matchId(odds.getMatchId())
                .type(HandicapType.parse(odds.getType()))
                .oddsType(odds.getOddsType())
                .isMaintenance(odds.getMaintenance() == null ? Boolean.FALSE : odds.getMaintenance())
                .isClose(odds.getIsClose() == null ? Boolean.FALSE : odds.getIsClose())
                .latestChangeTime(odds.getChangeTime())
                .latestUpdateTime(odds.getLastResTime() == null ? LocalDateTime.now() : odds.getLastResTime())
                .build();
    }

    protected void checkOdds(OddsCheckInfo checkInfo, BetBo bo) {
        HandicapType handicapType = bo.getHandicapType();
        log.info("odds type {} bo.type {}", checkInfo.getType(), handicapType);
        BizAssert.isTrue(handicapType == checkInfo.getType(), BizErrCode.PARAM_ERROR_DESC, "handicapType");
        // 未返回是否关闭，是否当未关闭处理？
        log.info("matchId {} close {}",checkInfo.getMatchId(), checkInfo.isClose());
        // 投注是否关闭
        Schedules schedule = BetCache.getSchedule();
        boolean isClose = checkInfo.isClose();
        BizAssert.isTrue(!isClose, BizErrCode.ODDS_CLOSE);
        // 重新根据比赛状态判定是否应该关闭
        isClose = OddsAssist.rejudgmentClose(checkInfo.isClose(), schedule.getStatus(), bo.getHandicapType(), checkInfo.getOddsType());
        BizAssert.isTrue(!isClose, BizErrCode.SCHEDULE_CANNT_BET);
        // 是否维护
        BizAssert.isTrue(!checkInfo.isMaintenance(), BizErrCode.ODDS_MAINTENANCE);

        // 更新时间校验
        if (getAllowDelay() > 0) {
            LocalDateTime latestUpdateTime = checkInfo.getLatestUpdateTime();
            boolean delay = latestUpdateTime.plusSeconds(getAllowDelay()).isBefore(bo.getBetTime());
            log.info("bizNo {} latestUpdateTime {} allowDelay {} delay {}",bo.getBizNo(), latestUpdateTime, getAllowDelay(), delay);
            BizAssert.isTrue(!delay, BizErrCode.ODDS_DELAY);
        }
    }

    protected void checkSchedule(BetBo bo, String matchId) {
        HandicapType handicapType = bo.getHandicapType();

        Schedules schedules = schedulesService.queryOne(matchId);
        BetCache.setSchedule(schedules);
        Integer status = schedules.getStatus();
        log.info("matchId {} status {}", schedules.getMatchId(), status);
    }

    protected Long getAllowDelay() {
        return allowDelay;
    }

    public String getMatchId(String bizNo, String matchId) {
        if (StringUtils.isEmpty(matchId)) {
            matchId = Optional.ofNullable(oddsService.queryByBizNo(bizNo)).map(Odds::getMatchId).orElse(null);
        }
        return matchId;
    }

    protected void checkUser(BetBo bo, Integer oddsType) {
        // 用户状态
        UserInfo user = userInfoService.getByUid(bo.getUserNo());
        log.info("userId {} status {}", bo.getUserNo(), user.getStatus());
        BizAssert.isTrue(YesOrNo.YES.v == user.getStatus(), BizErrCode.USER_LOCKED);

        // 用户余额，总投注限额
        UserAccount account = userAccountService.query(bo.getUserNo());
        BizAssert.notNull(account, BizErrCode.ACCOUNT_NOT_EXIST);
        BetCache.setUserAccount(account);
        log.info("userId {} betAmount {} balance {} freeze {}", bo.getUserNo(), bo.getBetAmount(), account.getBalance(), account.getFreezeAmount());
        BizAssert.isTrue(account.getBalance().subtract(account.getFreezeAmount()).compareTo(bo.getBetAmount()) >= 0, BizErrCode.ACCOUNT_BALANCE_INSUFFICIENT);

        // 单场限额
        PlayTypeEnum playTypeEnum = OrderHelper.getPlayTypeEnum(bo.getHandicapType(), oddsType);
        log.info("userId {} playTypeEnum {}", bo.getUserNo(), playTypeEnum);
        if (playTypeEnum != null) {
            TradeConfig tradeConfig = tradeConfigService.getUserConfig(bo.getUserNo(), SportEnum.FOOTBALL, playTypeEnum);
            log.info("userId {} tradeConfig not null {}", bo.getUserNo(), tradeConfig != null);
            if (tradeConfig != null) {
                BigDecimal betMin = tradeConfig.getMin();
                BigDecimal betMax = tradeConfig.getOrderLimit();
                // 用户单项投注限额，最大，最小
                log.info("userId {} betAmount {} min {} max {}", bo.getUserNo(), bo.getBetAmount(), betMin, betMax);
                BizAssert.isTrue(bo.getBetAmount().compareTo(betMin) >= 0, BizErrCode.BET_AMOUNT_TOO_MIN, betMin.stripTrailingZeros().toPlainString(), account.getCurrency());
                BizAssert.isTrue(bo.getBetAmount().compareTo(betMax) <= 0, BizErrCode.BET_AMOUNT_TOO_MAX, betMin.stripTrailingZeros().toPlainString(), account.getCurrency());
                // 单场已投注
                BigDecimal matchBetAmount = orderInfoService.statBetAmount(bo.getUserNo(), bo.getMatchId());
                log.info("userId {} betAmount {} matchId {} matchBetAmount {} matchBetMax {}", bo.getUserNo(), bo.getBetAmount(), bo.getMatchId(), matchBetAmount, tradeConfig.getMatchLimit());
                BizAssert.isTrue(matchBetAmount.add(bo.getBetAmount()).compareTo(tradeConfig.getMatchLimit()) <= 0, BizErrCode.MATCH_BET_AMOUNT_TOO_MAX);
            }
        }
    }

    protected OrderInfo buildOrder(BetBo bo, String orderNo) {
        log.info("start");
        long start = System.currentTimeMillis();
        BetInfo betInfo = getBetInfo(bo);
        log.info("betInfo {}", JSON.toJSONString(betInfo));

        OrderInfo order = new OrderInfo();
        order.setOrderId(orderNo);
        order.setUserId(bo.getUserNo());

        setProxy(order, bo.getUserNo());

        order.setSport(Sport.FOOTBALL.getCode());

        order.setLeagueId(betInfo.getLeagueId());
        order.setMatchId(betInfo.getMatchId());
        order.setCompanyId(betInfo.getCompanyId());
        order.setHandicapType(bo.getHandicapType().getCode());
        order.setBetOption(bo.getBetOption().name());
        order.setBetAmount(bo.getBetAmount());
        order.setBetOdds(new BigDecimal(betInfo.getBetOddsStr()));
        order.setOddsType(betInfo.getOddsType());

        Schedules schedules = schedulesService.queryOne(betInfo.getMatchId());
        order.setHomeCurrentScore(schedules.getHomeScore());
        order.setAwayCurrentScore(schedules.getAwayScore());
        order.setScheduleStatus(schedules.getStatus());

        order.setOddsData(betInfo.getOddsData());
        order.setInstantHandicap(betInfo.getInstantHandicap());
        order.setBetDate(LocalDate.now());

        order.setBetCurrency(BetCache.getUserAccount().getCurrency());

        log.info("end spend time {}",(System.currentTimeMillis() - start));
        return order;
    }

    protected void setProxy(OrderInfo order, Long userId) {
        UserInfo user = userInfoService.getByUid(userId);
        if (!StringUtils.isEmpty(user.getProxyInfo())) {
            String[] split = user.getProxyInfo().split("#");
            int len = split.length;
            if (len == 3) {
                order.setProxy1(Longs.tryParse(split[0]));
                order.setProxy2(Longs.tryParse(split[1]));
                order.setProxy3(Longs.tryParse(split[2]));
            } else if (len == 2) {
                order.setProxy1(Longs.tryParse(split[0]));
                order.setProxy2(Longs.tryParse(split[1]));
            } else if (len == 1) {
                order.setProxy1(Longs.tryParse(split[0]));
            }
        }
    }

    /**
     * 抽取投注信息
     * @param bo
     * @return
     */
    protected BetInfo getBetInfo(BetBo bo) {
        Odds odds = oddsService.queryByBizNo(bo.getBizNo());
        String oddsData = JSON.toJSONString(BeanUtil.copy(odds, OddsData.class));
        String matchId = odds.getMatchId();
        String companyId = odds.getCompanyId();

        String betOddsStr = getBetOdds(odds, bo);
        String handicapStr = OrderHelper.translate(odds.getInstantHandicap());
        log.info("type {} betOption {} betOdds {} instantHandicap {} handicapStr {}", bo.getHandicapType(), bo.getBetOption(), betOddsStr, odds.getInstantHandicap(), handicapStr);
        return BetInfo.builder()
                .oddsData(oddsData)
                .leagueId(getLeagueId(matchId))
                .matchId(matchId)
                .companyId(companyId)
                .betOddsStr(betOddsStr)
                .instantHandicap(handicapStr)
                .oddsType(odds.getOddsType())
                .build();
    }

    protected String getLeagueId(String matchId) {
        Schedules schedule = BetCache.getSchedule();
        if (schedule == null) {
            schedule = schedulesService.queryOne(matchId);
        }
        return schedule.getLeagueId();
    }

    /**
     * 根据盘口类型，投注选项，取对应即时赔率
     * @param odds
     * @param bo
     * @return
     */
    protected String getBetOdds(Odds odds, BetBo bo) {
        throw new BizException(BizErrCode.NOT_FOUND_BET_OPTION);
    }

    /**
     * 各实现控制
     * @return
     */
    protected boolean isEnable() {
        return true;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        BetProcessorHolder.register(getHandicapType(), this);
    }
}
