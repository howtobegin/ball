package com.ball.biz.account.service;

import com.ball.biz.account.entity.Currency;
import com.baomidou.mybatisplus.extension.service.IService;

import com.ball.common.service.IBaseService;

import java.math.BigDecimal;

/**
 * <p>
 * 币种 服务类
 * </p>
 *
 * @author atom
 * @since 2022-10-19
 */
public interface ICurrencyService extends IService<Currency>, IBaseService {

    /**
     * 查询汇率
     * @param currencyCode 币种
     * @return
     */
    BigDecimal getRmbRate(String currencyCode);

    /**
     * 获取用户币种对USD的汇率 ,精度6位小数，向下取整
     * @param userNo
     * @return
     */
    BigDecimal getUserUsdRate(Long userNo);
}
