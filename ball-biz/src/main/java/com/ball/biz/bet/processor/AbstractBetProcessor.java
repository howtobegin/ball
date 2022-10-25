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
import com.ball.biz.bet.enums.BetType;
import com.ball.biz.bet.order.bo.BetBo;
import com.ball.biz.bet.order.bo.Handicap;
import com.ball.biz.bet.order.bo.OddsData;
import com.ball.biz.bet.order.settle.assist.OverUnderAssist;
import com.ball.biz.bet.processor.bo.BetInfo;
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
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
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

    protected void betCheck(BetBo bo) {
        // 校验投注信息，是否存在，关闭等
        checkOdds(bo);

        // 投注选项是否合理
        BizAssert.isTrue(bo.getHandicapType().getBetOptions().contains(bo.getBetOption()), BizErrCode.PARAM_ERROR_DESC, "betOption");

        // 校验用户状态，余额等
        checkUser(bo);
    }

    protected void checkUser(BetBo bo) {
        // 用户状态
        UserInfo user = userInfoService.getByUid(bo.getUserNo());
        BizAssert.isTrue(YesOrNo.YES.v == user.getStatus(), BizErrCode.USER_LOCKED);
        // TODO 用户单项投注限额

        // 用户余额，总投注限额
        UserAccount account = userAccountService.query(bo.getUserNo());
        BizAssert.notNull(account, BizErrCode.ACCOUNT_NOT_EXIST);
        BizAssert.isTrue(account.getBalance().subtract(account.getFreezeAmount()).compareTo(bo.getBetAmount()) >= 0, BizErrCode.ACCOUNT_BALANCE_INSUFFICIENT);
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
    }

    protected OrderInfo buildOrder(BetBo bo, String orderNo) {
        log.info("start");
        long start = System.currentTimeMillis();
        BetInfo betInfo = getBetInfo(bo);
        log.info("betInfo {}", JSON.toJSONString(betInfo));

        OrderInfo order = new OrderInfo();
        order.setOrderId(orderNo);
        order.setUserId(bo.getUserNo());

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
        String handicapStr = null;
        if (!StringUtils.isEmpty(odds.getInstantHandicap())) {
            Handicap handicap = OverUnderAssist.analyzeHandicap(new BigDecimal(odds.getInstantHandicap()).abs());
            String bigStr = doubleToString(handicap.getBig());
            String smallStr = doubleToString(handicap.getSmall());
            handicapStr = handicap.getBetType() == BetType.ALL ? bigStr : smallStr + "/" + bigStr;
            if (odds.getInstantHandicap().startsWith("-")) {
                handicapStr = "-" + handicapStr;
            }
        }
        log.info("type {} betOption {} betOdds {} instantHandicap {} handicapStr {}", bo.getHandicapType(), bo.getBetOption(), betOddsStr, odds.getInstantHandicap(), handicapStr);
        return BetInfo.builder()
                .oddsData(oddsData)
                .matchId(matchId)
                .companyId(companyId)
                .betOddsStr(betOddsStr)
                .instantHandicap(handicapStr)
                .build();
    }

    private String doubleToString(Double value) {
        return Optional.ofNullable(value)
                .map(BigDecimal::valueOf)
                .map(BigDecimal::stripTrailingZeros)
                .map(BigDecimal::toPlainString)
                .orElse("");
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
