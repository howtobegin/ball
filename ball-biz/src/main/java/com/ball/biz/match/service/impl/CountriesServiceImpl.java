package com.ball.biz.match.service.impl;

import com.ball.biz.match.entity.Countries;
import com.ball.biz.match.mapper.CountriesMapper;
import com.ball.biz.match.service.ICountriesService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author lhl
 * @since 2022-10-19
 */
@Service
public class CountriesServiceImpl extends ServiceImpl<CountriesMapper, Countries> implements ICountriesService {

}
