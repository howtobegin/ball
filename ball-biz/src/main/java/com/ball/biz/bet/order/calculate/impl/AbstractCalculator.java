package com.ball.biz.bet.order.calculate.impl;

import com.alibaba.fastjson.JSON;
import com.ball.base.util.BizAssert;
import com.ball.biz.account.entity.TradeConfig;
import com.ball.biz.account.enums.PlayTypeEnum;
import com.ball.biz.account.enums.SportEnum;
import com.ball.biz.account.enums.UserLevelEnum;
import com.ball.biz.account.service.ITradeConfigService;
import com.ball.biz.bet.enums.BetResult;
import com.ball.biz.bet.enums.HandicapType;
import com.ball.biz.bet.order.OrderHelper;
import com.ball.biz.bet.order.bo.ProxyAmount;
import com.ball.biz.bet.order.calculate.Calculator;
import com.ball.biz.bet.order.calculate.CalculatorHolder;
import com.ball.biz.bet.order.calculate.bo.CalcBo;
import com.ball.biz.bet.order.calculate.bo.CalcResult;
import com.ball.biz.exception.BizErrCode;
import com.ball.biz.user.bo.ProxyRateInfo;
import com.ball.biz.user.proxy.ProxyUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.math.BigDecimal;
import java.util.Optional;

/**
 * @author lhl
 * @date 2022/10/19 下午5:53
 */
@Slf4j
public abstract class AbstractCalculator implements Calculator, InitializingBean {
    protected static final BigDecimal HALF = BigDecimal.valueOf(0.5);

    @Autowired
    protected ProxyUserService proxyUserService;
    @Autowired
    protected ITradeConfigService tradeConfigService;

    @Value("${resultAmount.scale:2}")
    protected int scale;

    @Override
    public CalcResult calc(CalcBo bo) {
        log.info("bo {} ", JSON.toJSONString(bo));
        calcCheck(bo.getBetAmount(), bo.getOdds());
        // 计算输赢
        CalcResult calcResult = doCalc(bo.getBetAmount(), bo.getOdds());
        // 计算代理占成
        calcProxyAmount(bo.getUserId(), calcResult);
        // 计算退水
        calcBackwaterAmount(bo, calcResult);
        return calcResult;
    }

    /**
     * 计算输赢
     * @param betAmount
     * @param odds
     * @return
     */
    protected abstract CalcResult doCalc(BigDecimal betAmount, BigDecimal odds);

    /**
     * 计算代理占成
     */
    protected CalcResult calcProxyAmount(Long userId, CalcResult calcResult) {
        BigDecimal resultAmount = getResultAmount(calcResult);
        if (calcResult.getResult() == BetResult.DRAW) {
            calcResult.setProxyAmount(ProxyAmount.init());
            return calcResult;
        }
        ProxyRateInfo proxyRateInfo = proxyUserService.getProxyRateByUid(userId);
        log.info("userId {} resultAmount {} proxyRateInfo {}", userId, resultAmount, JSON.toJSON(proxyRateInfo));

        // 占成百分比
        BigDecimal proxy1Percent = Optional.ofNullable(proxyRateInfo).map(ProxyRateInfo::getProxyOneRate).orElse(BigDecimal.ZERO);
        BigDecimal proxy2Percent = Optional.ofNullable(proxyRateInfo).map(ProxyRateInfo::getProxyTwoRate).orElse(BigDecimal.ZERO);
        BigDecimal proxy3Percent = Optional.ofNullable(proxyRateInfo).map(ProxyRateInfo::getProxyThreeRate).orElse(BigDecimal.ZERO);

        // 符号去反
        BigDecimal proxy1Amount = BigDecimal.ZERO.subtract(resultAmount.multiply(proxy1Percent));
        BigDecimal proxy2Amount = BigDecimal.ZERO.subtract(resultAmount.multiply(proxy2Percent));
        BigDecimal proxy3Amount = BigDecimal.ZERO.subtract(resultAmount.multiply(proxy3Percent));
        log.info("resultAmount {} p1 {} p2 {} p3 {}", resultAmount, proxy1Amount, proxy2Amount, proxy3Amount);
        calcResult.setProxyAmount(ProxyAmount.builder()
                .amount(resultAmount)
                .proxy1Percent(proxy1Percent)
                .proxy2Percent(proxy2Percent)
                .proxy3Percent(proxy3Percent)
                .proxy1Amount(proxy1Amount)
                .proxy2Amount(proxy2Amount)
                .proxy3Amount(proxy3Amount)
                .build());
        return calcResult;
    }

    protected void calcBackwaterAmount(CalcBo bo, CalcResult calcResult) {
        HandicapType type = HandicapType.parse(bo.getHandicapType());
        PlayTypeEnum playTypeEnum = OrderHelper.getPlayTypeEnum(type, bo.getOddsType());
        // 计算退水的基础金额
        BigDecimal backwaterBaseAmount = getBackwaterBaseAmount(calcResult);
        log.info("userId {} backwaterBaseAmont {} handicapType {} oddsType {} playTypeEnum {}", bo.getUserId(), backwaterBaseAmount, type, bo.getOddsType(), playTypeEnum);
        if (playTypeEnum == null || backwaterBaseAmount == null || backwaterBaseAmount.compareTo(BigDecimal.ZERO) == 0) {
            calcResult.setBackwaterAmount(BigDecimal.ZERO);
            return;
        }
        // 退水配置
        TradeConfig config = tradeConfigService.getUserConfig(bo.getUserId(), SportEnum.FOOTBALL, playTypeEnum);
        log.info("config {}", JSON.toJSONString(config));
        BigDecimal backwaterPercent = Optional.ofNullable(config)
                .map(TradeConfig::getUserLevel)
                .map(l -> {
                    log.info("userId {} userLevel", bo.getUserId(), l);
                    UserLevelEnum e = UserLevelEnum.valueOf(l);
                    return UserLevelEnum.A == e ? config.getA() : UserLevelEnum.B == e ? config.getB() : UserLevelEnum.C == e ? config.getC() : config.getD();
                }).orElse(BigDecimal.ZERO);
        log.info("backwaterBaseAmont {} backwaterPercent {} hasConfig {}",backwaterBaseAmount, backwaterPercent, config != null);
        BizAssert.isTrue(backwaterPercent.compareTo(BigDecimal.ZERO) >= 0, BizErrCode.TRADE_CONFIG_BACKWATER_PERCENT_MUST_BIGGER_ZERO);
        // 退水
        BigDecimal backwaterAmount = backwaterBaseAmount.multiply(backwaterPercent);
        calcResult.setBackwaterAmount(backwaterAmount);
        return;
    }

    protected abstract BigDecimal getBackwaterBaseAmount(CalcResult calcResult);

    protected BigDecimal getResultAmount(CalcResult calcResult) {
        return calcResult.getResultAmount();
    }

    protected void calcCheck(BigDecimal betAmount, BigDecimal odds) {
        BizAssert.notNull(betAmount, BizErrCode.PARAM_ERROR_DESC,"betAmount");
        BizAssert.notNull(odds, BizErrCode.PARAM_ERROR_DESC,"rate");

        BizAssert.isTrue(betAmount.compareTo(BigDecimal.ZERO) > 0, BizErrCode.PARAM_ERROR_DESC,"betAmount");
        BizAssert.isTrue(odds.compareTo(BigDecimal.ZERO) > 0, BizErrCode.PARAM_ERROR_DESC,"rate");
    }

    protected BigDecimal handleScale(BigDecimal amount) {
        return amount.setScale(scale, BigDecimal.ROUND_DOWN);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        CalculatorHolder.register(getBetResult(), this);
    }
}
