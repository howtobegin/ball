package com.ball.biz.base.service;

import com.ball.biz.base.entity.IdGen;
import com.baomidou.mybatisplus.extension.service.IService;

import com.ball.common.service.IBaseService;

/**
 * <p>
 * 编号生成表 服务类
 * </p>
 *
 * @author JimChery
 * @since 2022-10-19
 */
public interface IIdGenService extends IService<IdGen>, IBaseService {
    long get(TableNameEnum tableName);
}
