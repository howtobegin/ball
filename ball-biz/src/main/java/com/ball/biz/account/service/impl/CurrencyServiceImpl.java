package com.ball.biz.account.service.impl;

import com.ball.biz.account.entity.Currency;
import com.ball.biz.account.mapper.CurrencyMapper;
import com.ball.biz.account.service.ICurrencyService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

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

}
