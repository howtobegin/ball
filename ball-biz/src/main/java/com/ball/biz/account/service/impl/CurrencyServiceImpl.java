package com.ball.biz.account.service.impl;

import com.ball.base.util.BizAssert;
import com.ball.biz.account.entity.Currency;
import com.ball.biz.account.entity.UserAccount;
import com.ball.biz.account.mapper.CurrencyMapper;
import com.ball.biz.account.service.ICurrencyService;
import com.ball.biz.account.service.IUserAccountService;
import com.ball.biz.enums.CurrencyEnum;
import com.ball.biz.exception.BizErrCode;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * <p>
 * 币种 服务实现类
 * </p>
 *
 * @author atom
 * @since 2022-10-19
 */
@Service
public class CurrencyServiceImpl extends ServiceImpl<CurrencyMapper, Currency> implements ICurrencyService {

    @Autowired
    private IUserAccountService iUserAccountService;


    /**
     * 查询汇率
     *
     * @param currencyCode 币种
     * @return
     */
    @Override
    public BigDecimal getRmbRate(String currencyCode) {
        Currency currency = lambdaQuery().eq(Currency::getCurrencyCode, currencyCode).one();
        BizAssert.isTrue(currency!=null, BizErrCode.CURRENCY_NOT_FOUND);
        return currency.getRate();
    }

    /**
     * 获取用户币种对USD的汇率 ,精度6位小数，向下取整
     *
     * @param userNo
     * @return
     */
    @Override
    public BigDecimal getUserUsdRate(Long userNo) {
        UserAccount userAccount = iUserAccountService.lambdaQuery().eq(UserAccount::getUserId, userNo).one();
        BizAssert.notNull(userAccount, BizErrCode.ACCOUNT_NOT_EXIST);
        BigDecimal usdRmb = getRmbRate(CurrencyEnum.USD.name()).stripTrailingZeros();
        BigDecimal userRmb = getRmbRate(userAccount.getCurrency()).stripTrailingZeros();

        return userRmb.divide(usdRmb,6, RoundingMode.FLOOR);
    }
}
