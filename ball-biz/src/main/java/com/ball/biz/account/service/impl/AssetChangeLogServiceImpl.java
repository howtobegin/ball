package com.ball.biz.account.service.impl;

import com.ball.biz.account.entity.AssetChangeLog;
import com.ball.biz.account.mapper.AssetChangeLogMapper;
import com.ball.biz.account.service.IAssetChangeLogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 出入金日志表 服务实现类
 * </p>
 *
 * @author atom
 * @since 2022-10-19
 */
@Service
public class AssetChangeLogServiceImpl extends ServiceImpl<AssetChangeLogMapper, AssetChangeLog> implements IAssetChangeLogService {

}
