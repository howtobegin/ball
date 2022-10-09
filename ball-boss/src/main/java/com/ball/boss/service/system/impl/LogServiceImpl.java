package com.ball.boss.service.system.impl;


import com.ball.boss.dao.entity.BossCheckLog;
import com.ball.boss.dao.entity.BossOperationLog;
import com.ball.boss.dao.ext.CheckLogMapper;
import com.ball.boss.service.IBossCheckLogService;
import com.ball.boss.service.IBossOperationLogService;
import com.ball.boss.service.system.BaseService;
import com.ball.boss.service.system.LogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class LogServiceImpl extends BaseService implements LogService {
    @Autowired
    private CheckLogMapper checkLogMapper;

    @Autowired
    private IBossOperationLogService operationLogService;

    @Autowired
    private IBossCheckLogService checkLogService;


    @Override
    public List<BossOperationLog> queryLogByBizId(String bizId, String bizType) {
        log.info("LogService queryLogByBizId bizId={}, bizType={}", bizId, bizType);
        return operationLogService.lambdaQuery().eq(BossOperationLog::getBizId, bizId)
                .eq(BossOperationLog::getOperationBiz, bizType)
                .orderByDesc(BossOperationLog::getId)
                .list();
    }

    @Override
    public List<BossCheckLog> queryCheckLogByBizId(String bizId, String bizCode) {
        log.info("LogService queryLogByBizId bizId={}, bizCode={}", bizId, bizCode);
        return checkLogService.lambdaQuery().eq(BossCheckLog::getBizId, bizId)
                .eq(BossCheckLog::getBizCode, bizCode)
                .orderByDesc(BossCheckLog::getId)
                .list();
    }

    @Override
    public Map<String, List<BossCheckLog>> queryCheckLogByBizIds(List<String> bizIds, String bizCode) {
        List<BossCheckLog> checkLogPos = checkLogMapper.selectByBizIds(bizCode, bizIds);
        if (!CollectionUtils.isEmpty(checkLogPos)) {
            return checkLogPos.stream().collect(Collectors.groupingBy(BossCheckLog::getBizId));
        }
        return new HashMap<>();
    }
}
