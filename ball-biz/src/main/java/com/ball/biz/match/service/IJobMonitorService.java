package com.ball.biz.match.service;

import com.ball.biz.bet.enums.JobEnum;
import com.ball.biz.match.entity.JobMonitor;
import com.baomidou.mybatisplus.extension.service.IService;

import com.ball.common.service.IBaseService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author lhl
 * @since 2022-11-07
 */
public interface IJobMonitorService extends IService<JobMonitor>, IBaseService {

    JobMonitor queryOne(JobEnum jobEnum);

}
