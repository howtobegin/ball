package com.ball.biz.bet.processor;

import com.alibaba.fastjson.JSON;
import com.ball.base.model.enums.YesOrNo;
import com.ball.base.transaction.TransactionSupport;
import com.ball.base.util.BeanUtil;
import com.ball.base.util.BizAssert;
import com.ball.base.util.IDCreator;
import com.ball.biz.account.entity.UserAccount;
import com.ball.biz.account.enums.AccountTransactionType;
import com.ball.biz.account.service.IUserAccountService;
import com.ball.biz.bet.enums.MatchTimeType;
import com.ball.biz.bet.enums.ScheduleStatus;
import com.ball.biz.bet.order.OrderHelper;
import com.ball.biz.bet.order.bo.BetBo;
import com.ball.biz.bet.order.bo.OddsData;
import com.ball.biz.bet.processor.bo.BetInfo;
import com.ball.biz.bet.processor.cache.UserProxyCache;
import com.ball.biz.exception.BizErrCode;
import com.ball.biz.exception.BizException;
import com.ball.biz.match.entity.Odds;
import com.ball.biz.match.entity.Schedules;
import com.ball.biz.match.service.IOddsScoreService;
import com.ball.biz.match.service.IOddsService;
import com.ball.biz.match.service.ISchedulesService;
import com.ball.biz.order.entity.OrderInfo;
import com.ball.biz.order.service.IOrderInfoService;
import com.ball.biz.user.entity.UserInfo;
import com.ball.biz.user.service.IUserInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.math.BigDecimal;

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
    private UserProxyCache userProxyCache;

    /**
     * 单注最低
     */
    @Value("${bet.min:50}")
    private BigDecimal betMin;
    /**
     * 单注最高
     */
    @Value("${bet.max:25000}")
    private BigDecimal betMax;
    /**
     * 单场最高
     */
    @Value("${bet.max:50000}")
    private BigDecimal matchBetMax;

    /**
     * 投注
     * @param bo
     * @return
     */
    @Override
    public OrderInfo bet(BetBo bo) {
        log.info("betBo {}", JSON.toJSON(bo));

        betCheck(bo);
        String orderNo = IDCreator.get();
        BigDecimal fee = BigDecimal.ZERO;
        // 构建订单信息，状态待确认
        OrderInfo order = buildOrder(bo, orderNo);
        transactionSupport.execute(()->{
            // 保存订单信息
            orderInfoService.save(order);
            // 冻结
            userAccountService.freeze(bo.getUserNo(), bo.getBetAmount(), orderNo, fee, AccountTransactionType.TRADE);
        });
        return order;
    }

    private void betCheck(BetBo bo) {
        // 位置不要挪动，校验投注信息，是否存在，关闭等，拿到matchId，后面用
        checkOdds(bo);
        // 校验赛事
        checkSchedule(bo);
        // 投注选项是否合理
        BizAssert.isTrue(bo.getHandicapType().getBetOptions().contains(bo.getBetOption()), BizErrCode.PARAM_ERROR_DESC, "betOption");
        // 校验用户状态，余额等
        checkUser(bo);
    }

    protected void checkSchedule(BetBo bo) {
        Schedules schedules = schedulesService.queryOne(bo.getMatchId());
        Integer status = schedules.getStatus();
        // 全场，校验状态
        if (bo.getHandicapType().getMatchTimeType() == MatchTimeType.FULL) {
            BizAssert.isTrue(ScheduleStatus.canBetCodes().contains(status), BizErrCode.SCHEDULE_CANNT_BET);
        }
        // 半场，校验状态
        if (bo.getHandicapType().getMatchTimeType() == MatchTimeType.HALF) {
            BizAssert.isTrue(ScheduleStatus.halfCanBetCodes().contains(status), BizErrCode.SCHEDULE_CANNT_BET);
        }
    }

    protected void checkUser(BetBo bo) {
        // 用户状态
        UserInfo user = userInfoService.getByUid(bo.getUserNo());
        BizAssert.isTrue(YesOrNo.YES.v == user.getStatus(), BizErrCode.USER_LOCKED);
        // 用户单项投注限额，最大，最小
        BizAssert.isTrue(bo.getBetAmount().compareTo(betMin) >= 0, BizErrCode.BET_AMOUNT_TOO_MIN);
        BizAssert.isTrue(bo.getBetAmount().compareTo(betMax) <= 0, BizErrCode.BET_AMOUNT_TOO_MAX);

        // 用户余额，总投注限额
        UserAccount account = userAccountService.query(bo.getUserNo());
        BizAssert.notNull(account, BizErrCode.ACCOUNT_NOT_EXIST);
        BizAssert.isTrue(account.getBalance().subtract(account.getFreezeAmount()).compareTo(bo.getBetAmount()) >= 0, BizErrCode.ACCOUNT_BALANCE_INSUFFICIENT);

        // 单场限额
        // 单场已投注
        BigDecimal betAmount = orderInfoService.statBetAmount(bo.getMatchId());
        BizAssert.isTrue(betAmount.add(bo.getBetAmount()).compareTo(matchBetMax) <= 0, BizErrCode.MATCH_BET_AMOUNT_TOO_MAX);
    }

    protected void checkOdds(BetBo bo) {
        Odds odds = oddsService.queryByBizNo(bo.getBizNo());
        BizAssert.notNull(odds, BizErrCode.DATA_NOT_EXISTS);
        log.info("odds type {} bo.type {}", odds.getType(), bo.getHandicapType());
        BizAssert.isTrue(bo.getHandicapType().isMe(odds.getType()), BizErrCode.PARAM_ERROR_DESC, "handicapType");
        // 未返回是否关闭，是否当未关闭处理？
        boolean isClose = odds.getIsClose() == null ? Boolean.FALSE : odds.getIsClose();
        // 投注是否关闭
        BizAssert.isTrue(!isClose, BizErrCode.ODDS_CLOSE);

        bo.setMatchId(odds.getMatchId());
    }

    protected OrderInfo buildOrder(BetBo bo, String orderNo) {
        log.info("start");
        long start = System.currentTimeMillis();
        BetInfo betInfo = getBetInfo(bo);
        log.info("betInfo {}", JSON.toJSONString(betInfo));

        OrderInfo order = new OrderInfo();
        order.setOrderId(orderNo);
        order.setUserId(bo.getUserNo());
        order.setProxy3(userProxyCache.getProxy(order.getUserId()));
        order.setProxy2(userProxyCache.getProxy(order.getProxy3()));
        order.setProxy1(userProxyCache.getProxy(order.getProxy2()));

        order.setMatchId(betInfo.getMatchId());
        order.setCompanyId(betInfo.getCompanyId());
        order.setHandicapType(bo.getHandicapType().getCode());
        order.setBetOption(bo.getBetOption().name());
        order.setBetAmount(bo.getBetAmount());
        order.setBetOdds(new BigDecimal(betInfo.getBetOddsStr()));

        Schedules schedules = schedulesService.queryOne(betInfo.getMatchId());
        order.setHomeCurrentScore(schedules.getHomeScore());
        order.setAwayCurrentScore(schedules.getAwayScore());
        order.setScheduleStatus(schedules.getStatus());

        order.setOddsData(betInfo.getOddsData());
        order.setInstantHandicap(betInfo.getInstantHandicap());
        log.info("end spend time {}",(System.currentTimeMillis() - start));
        return order;
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
                .matchId(matchId)
                .companyId(companyId)
                .betOddsStr(betOddsStr)
                .instantHandicap(handicapStr)
                .build();
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

    @Override
    public void afterPropertiesSet() throws Exception {
        BetProcessorHolder.register(getHandicapType(), this);
    }

    protected void logInfo(String message, String...args) {
        log.info("processor {} " + message, getHandicapType(), args);
    }
}
