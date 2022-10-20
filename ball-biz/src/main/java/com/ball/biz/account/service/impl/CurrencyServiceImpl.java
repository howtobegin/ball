package com.ball.biz.account.service.impl;

import com.ball.base.util.BizAssert;
import com.ball.biz.account.entity.Currency;
import com.ball.biz.account.mapper.CurrencyMapper;
import com.ball.biz.account.service.ICurrencyService;
import com.ball.biz.exception.BizErrCode;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

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

    /**
     * 查询汇率
     *
     * @param currencyCode 币种
     * @return
     */
    @Override
    public BigDecimal getRate(String currencyCode) {
        Currency currency = lambdaQuery().eq(Currency::getCurrencyCode, currencyCode).one();
        BizAssert.isTrue(currency!=null, BizErrCode.CURRENCY_NOT_FOUND);
        return currency.getRate();
    }
}
