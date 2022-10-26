package com.ball.boss.service.system.impl;




import com.ball.base.exception.KickOutException;
import com.ball.base.util.BizAssert;
import com.ball.base.util.MessageUtils;
import com.ball.boss.dao.entity.BossKickOutLog;
import com.ball.boss.dao.entity.BossLoginSession;
import com.ball.boss.dao.entity.BossUserInfo;
import com.ball.boss.dao.entity.BossUserLockInfo;
import com.ball.boss.dao.ext.KickLogMapper;
import com.ball.boss.exception.BossErrorCode;
import com.ball.boss.service.IBossKickOutLogService;
import com.ball.boss.service.IBossLoginSessionService;
import com.ball.boss.service.IBossUserInfoService;
import com.ball.boss.service.IBossUserLockInfoService;
import com.ball.boss.service.assist.IpAssist;
import com.ball.boss.service.system.BaseService;
import com.ball.boss.service.system.LoginService;
import com.ball.boss.service.system.model.LockEnum;
import com.ball.boss.service.system.model.LockType;
import com.ball.boss.service.system.model.UserInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.ball.base.context.RequestContext.getIp;
import static com.ball.base.util.EncryptUtil.md5Plus;


/**
 * 登录服务
 * @author JimChery
 */
@Service
@Slf4j
public class LoginServiceImpl extends BaseService implements LoginService {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Value("${login.kick.out.message:您的账户在{terminateIp}[{address}]登录, 您之前登录的ip为{ip}}")
    private String kickOutMessage;

    @Value("${login.kick.out.time:1800}")
    private int kickLockTime;

    @Value("${login.kick.out.count:10}")
    private int kickLockCount;

    @Value("${login.kick.out.enabled:false}")
    private boolean kickEnabled;

    @Autowired
    private KickLogMapper kickLogMapper;

    @Autowired
    private IBossUserInfoService userInfoService;

    @Autowired
    private IBossLoginSessionService loginSessionService;

    @Autowired
    private IBossKickOutLogService kickOutLogService;

    @Autowired
    private IBossUserLockInfoService lockInfoService;

    @Autowired
    private IpAssist ipAssist;



    /**
     *
     * @param account   -  账户编号
     * @param password  -  密码
     * @param sessionId -  当前会话的session编号
     * @return
     */
    @Override
    public UserInfo login(String account, String password, String sessionId) {
        log.info("UserService login account={}", account);
        // 查询用户信息
        BossUserInfo userInfo = userInfoService.lambdaQuery().eq(BossUserInfo::getAccount, account).one();
        BizAssert.isTrue(userInfo != null && userInfo.getPassword().equals(md5Plus(password)), BossErrorCode.USER_OR_PASSWORD_ERROR);
        // 判断账户状态
        BizAssert.isTrue(LockEnum.isNotLock(userInfo.getLocked()), BossErrorCode.USER_ACCOUNT_LOCKED);
        // 查询用户登录信息
        BossLoginSession loginSession = loginSessionService.lambdaQuery().eq(BossLoginSession::getUserId, userInfo.getUserId()).one();
        BizAssert.notNull(loginSession, BossErrorCode.DATA_ERROR);
        transactionSupport.execute(() -> {
            // 处理踢出逻辑
            dealKickOut(userInfo.getUserId(), loginSession.getIp(), loginSession.getSessionId());
            // 更新loginSession信息
            loginSession.setIp(getIp());
            loginSession.setSessionId(sessionId);
            loginSession.setUpdateTime(null);
            loginSessionService.updateById(loginSession);
            // 记录登录日志
            logAssist.loginLog(userInfo.getUserId());
            // 解析登录ip地址
            ipAssist.checkOrSaveIpAddress(getIp());
        });

        return UserInfo.builder()
                .account(account)
                .sex(userInfo.getSex())
                .userId(userInfo.getUserId())
                .userName(userInfo.getUserName())
                .mobileArea(userInfo.getMobileArea())
                .mobilePhone(userInfo.getMobilePhone())
                .build();
    }

    @Override
    public void logout(String userId) {
        // 修改login session为SYSTEM
        // 查询用户登录信息
        BossLoginSession loginSession =loginSessionService.lambdaQuery().eq(BossLoginSession::getUserId, userId).one();
        String oldSessionId = loginSession.getSessionId();
        loginSession.setSessionId(DEPT_ID_DEFAULT);
        loginSession.setUpdateTime(null);
        loginSessionService.updateById(loginSession);
        // 删除redis key
        if (redisTemplate.hasKey(SPRING_SESSION_KEY_PREFIX + oldSessionId)) {
            log.info("logout out userId={}, sessionId={}", userId, oldSessionId);
            // 删除主要信息key
            redisTemplate.delete(SPRING_SESSION_KEY_PREFIX + oldSessionId);
            redisTemplate.delete(SPRING_SESSION_EXPIRE_PREFIX + oldSessionId);
        }
    }

    @Override
    public void checkLocked(String userId) {
        BossUserInfo userInfo = userInfoService.lambdaQuery().eq(BossUserInfo::getUserId, userId).one();
        BizAssert.notNull(userInfo, BossErrorCode.DATA_ERROR);
        // 判断账户状态
        BizAssert.isTrue(LockEnum.isNotLock(userInfo.getLocked()), BossErrorCode.USER_ACCOUNT_LOCKED);
    }

    @Override
    public void checkKickOut(String sessionId) {
        BossKickOutLog log = kickOutLogService.lambdaQuery().eq(BossKickOutLog::getSessionId, sessionId).one();
        if (log != null) {
            KickOutException.throwMe(getKickOutMessage(log));
        }
    }

    /**
     * 获取踢出提示
     * @param log  - 踢出日志
     * @return -
     */
    private String getKickOutMessage(BossKickOutLog log) {
        Map<String, String> params = new HashMap<>();
        // 判断ip是否相等
        params.put("ip", log.getIp());
        params.put("terminateIp", log.getTerminateIp());
        params.put("address", getIpAddress(log.getTerminateIp()));
        return MessageUtils.format(kickOutMessage, params);
    }

    /**
     * 获取ip对应的详细信息
     * @param terminateIp -
     * @return -
     */
    private String getIpAddress(String terminateIp) {
        try {
            return ipAssist.getIpAddressInfo(terminateIp);
        } catch (Exception e) {
            return "UNKNOWN";
        }
    }

    /**
     * 处理踢出
     * @param userId         -
     * @param oldIp          -
     * @param oldSessionId   -
     */
    private void dealKickOut(String userId, String oldIp, String oldSessionId) {
        // 如果是logout，则不需要处理
        if (DEPT_ID_DEFAULT.equals(oldSessionId) || !kickEnabled) {
            return;
        }
        // 判断key是否还存在, 如果还存在，则进行踢出操作
        if (redisTemplate.hasKey(SPRING_SESSION_KEY_PREFIX + oldSessionId)) {
            log.info("ready kick out the other login, userId={}, sessionId={}", userId, oldSessionId);
            // 删除主要信息key
            redisTemplate.delete(SPRING_SESSION_KEY_PREFIX + oldSessionId);
            redisTemplate.delete(SPRING_SESSION_EXPIRE_PREFIX + oldSessionId);
            // 写入踢出日志
            insertKickOut(userId, oldIp, oldSessionId);
            // 执行锁定账户逻辑
            dealLock(userId);
        }
    }

    /**
     * 将用户踢出
     * @param userId         - 用户编号
     * @param oldIp          - 上一次该用户登录的idp
     * @param oldSessionId   - 上一次该用户登录的sessionId
     */
    private void insertKickOut(String userId, String oldIp, String oldSessionId) {
        BossKickOutLog log = new BossKickOutLog()
                .setIp(oldIp)
                .setSessionId(oldSessionId)
                .setTerminateIp(getIp())
                .setUserId(userId);
        kickOutLogService.save(log);
    }

    /**
     * 处理锁定
     * @param userId -
     */
    private void dealLock(String userId) {
        Date date = new Date(System.currentTimeMillis() - kickLockTime * 1000);
        int count = kickLogMapper.countKickOut(userId, date);
        if (count >= kickLockCount) {
            // 锁定用户，并且写入锁定原因
            kickLogMapper.lockUser(userId);
            BossUserLockInfo lockInfo = new BossUserLockInfo()
                    .setUserId(userId)
                    .setLockType(LockType.SYSTEM.v)
                    .setOperatorId(LockType.SYSTEM.name())
                    .setOperatorName(LockType.SYSTEM.name())
                    .setRemark("因多次互相登录踢出被锁定");
            lockInfoService.save(lockInfo);

        }
    }

}
