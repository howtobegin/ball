package com.ball.biz.log.service;

import com.ball.biz.log.entity.OperationLog;
import com.ball.biz.log.enums.OperationBiz;
import com.baomidou.mybatisplus.extension.service.IService;

import com.ball.common.service.IBaseService;

/**
 * <p>
 * 操作日志表 服务类
 * </p>
 *
 * @author atom
 * @since 2022-10-21
 */
public interface IOperationLogService extends IService<OperationLog>, IBaseService {
    /**
     * 添加操作日志
     * @param biz     - 业务
     * @param bizId   - 业务编号
     * @param remark  - 备注
     */
    void addLog(OperationBiz biz, String bizId, String remark);

    /**
     * 添加操作日志
     * @param biz     - 业务
     * @param bizId   - 业务编号
     */
    void addLog(OperationBiz biz, String bizId);
}
