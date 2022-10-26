package com.ball.boss.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ball.boss.dao.entity.IpAddress;
import com.ball.boss.dao.mapper.IpAddressMapper;
import com.ball.boss.service.IIpAddressService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * ip地址信息表 服务实现类
 * </p>
 *
 * @author JimChery
 * @since 2021-09-22
 */
@Service
public class IpAddressServiceImpl extends ServiceImpl<IpAddressMapper, IpAddress> implements IIpAddressService {

}
