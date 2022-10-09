package com.ball.boss.service.system;


import com.ball.boss.context.BossUserContext;
import com.ball.boss.dao.entity.BossCheckLog;
import com.ball.boss.dao.entity.BossLoginLog;
import com.ball.boss.dao.entity.BossOperationLog;
import com.ball.boss.dao.entity.BossUserInfo;
import com.ball.boss.service.IBossCheckLogService;
import com.ball.boss.service.IBossLoginLogService;
import com.ball.boss.service.IBossOperationLogService;
import com.ball.boss.service.IBossUserInfoService;
import com.ball.boss.service.system.model.CheckResult;
import com.ball.boss.service.system.model.CheckType;
import com.ball.boss.service.system.model.OperationType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.List;

import static com.ball.base.context.RequestContext.getIp;
import static com.ball.base.context.RequestContext.getTerminal;


/**
 * 日志助手(登录日志和操作日志)
 */
@Component
@Slf4j
public class LogAssist {

    private static final int bizIdLength = 120;

    private static final int remarkLength = 500;

    @Autowired
    private IBossLoginLogService loginLogService;

    @Autowired
    private IBossUserInfoService userInfoService;

    @Autowired
    private IBossCheckLogService checkLogService;

    @Autowired
    private IBossOperationLogService operationLogService;

    /**
     * 登录日志记录
     *
     * @param userId -- 登录用户编号
     */
    public void loginLog(String userId) {
        BossLoginLog loginLog = new BossLoginLog()
                .setLoginIp(getIp())
                .setTerminal(getTerminal())
                .setUserId(userId);
        loginLogService.save(loginLog);
    }

    /**
     * @param operationType -- 操作类型
     * @param operationBiz  -- 操作业务
     * @param bizId         -- 业务编号
     * @param remark        -- 备注
     */
    public void operationLog(OperationType operationType, String operationBiz, String bizId, String remark) {
        if (bizId != null && bizId.length() > bizIdLength) {
            bizId = bizId.substring(0, bizIdLength);
        }
        if (remark != null && remark.length() > remarkLength) {
            remark = remark.substring(0, remarkLength);
        }
        BossOperationLog log = new BossOperationLog()
                .setOperationBiz(operationBiz)
                .setOperationType(operationType.name())
                .setUserId(BossUserContext.getUserId())
                .setBizId(bizId)
                .setRemark(remark);
        //查询用户编号对应的用户信息
        BossUserInfo userInfo = getUserInfo(log.getUserId());
        log.setUserName(userInfo.getUserName());
        operationLogService.save(log);
    }

    /**
     * @param operationType -- 操作类型
     * @param operationBiz  -- 操作业务
     * @param bizId         -- 业务编号
     */
    public void operationLog(OperationType operationType, String operationBiz, String bizId) {
        operationLog(operationType, operationBiz, bizId, null);
    }

    /**
     * 将异常捕获，让其日志记录不影响事务等信息
     * 用户对日志操作不是特别敏感的业务
     *
     * @see LogAssist#operationLog(OperationType, String, String, String)
     */
    public void noneExceptionOperationLog(OperationType operationType, String operationBiz, String bizId, String remark) {
        try {
            operationLog(operationType, operationBiz, bizId, remark);
        } catch (Exception e) {
            log.error("插入操作日志信息异常", e);
        }
    }

    /**
     * @see LogAssist#noneExceptionOperationLog(OperationType, String, String, String)
     */
    public void noneExceptionOperationLog(OperationType operationType, String operationBiz, String bizId) {
        noneExceptionOperationLog(operationType, operationBiz, bizId, null);
    }

    /**
     * @see LogAssist#noneExceptionOperationLog(OperationType, String, String, String)
     */
    public void noneExceptionOperationLog(OperationType operationType, String operationBiz, Number bizId) {
        noneExceptionOperationLog(operationType, operationBiz, "" + bizId, null);
    }

    /**
     * 审核日志记录
     *
     * @param bizCode     -- 业务代码
     * @param bizId       -- 业务编号
     * @param checkType   -- 审核类型
     * @param checkResult -- 审核结果
     * @param remark      -- 审核备注
     */
    public void checkLog(String bizCode, String bizId, CheckType checkType, CheckResult checkResult, String remark) {
        BossCheckLog checkLog = new BossCheckLog()
                .setBizCode(bizCode)
                .setBizId(bizId)
                .setCheckResult(checkResult.v)
                .setCheckType(checkType.v)
                .setCheckUserId(BossUserContext.getUserId())
                .setRemark(remark);
        //查询用户编号对应的用户信息
        BossUserInfo userInfoPo = getUserInfo(checkLog.getCheckUserId());
        checkLog.setCheckUserName(userInfoPo.getUserName());
        checkLogService.save(checkLog);
    }

    /**
     * @see LogAssist#checkLog(String, String, CheckType, CheckResult, String)
     */
    public void checkLog(String bizCode, Long bizId, CheckType checkType, CheckResult checkResult, String remark) {
        this.checkLog(bizCode, "" + bizId, checkType, checkResult, remark);
    }

    public List<BossOperationLog> queryByBizId(String operationBiz, String bizId) {
        return operationLogService.lambdaQuery().eq(BossOperationLog::getBizId, bizId)
                .eq(BossOperationLog::getOperationBiz, operationBiz)
                .list();
    }

    private BossUserInfo getUserInfo(String userId) {
        BossUserInfo userInfo = userInfoService.lambdaQuery().eq(BossUserInfo::getUserId, userId).one();
        Assert.isTrue(userInfo != null, "该操作用户不存在");
        return userInfo;
    }

    public List<BossOperationLog> batchQueryBizLog(List<String> bizIds, OperationType operationType, String operationBiz) {
        return operationLogService.lambdaQuery()
                .in(BossOperationLog::getBizId, bizIds)
                .eq(BossOperationLog::getOperationType, operationType.name())
                .eq(BossOperationLog::getOperationBiz, operationBiz)
                .list();
    }
}
