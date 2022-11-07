package com.ball.biz.match.service.impl;

import com.ball.biz.bet.enums.JobEnum;
import com.ball.biz.match.entity.JobMonitor;
import com.ball.biz.match.mapper.JobMonitorMapper;
import com.ball.biz.match.service.IJobMonitorService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author lhl
 * @since 2022-11-07
 */
@Service
public class JobMonitorServiceImpl extends ServiceImpl<JobMonitorMapper, JobMonitor> implements IJobMonitorService {
    @Override
    public JobMonitor queryOne(JobEnum jobEnum) {
        return lambdaQuery().eq(JobMonitor::getJob, jobEnum.name()).one();
    }
}
