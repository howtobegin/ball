package com.ball.boss.service.system;


import com.ball.boss.service.system.model.UserInfo;

/**
 * 用户登录service
 * @see org.springframework.session.data.redis.RedisIndexedSessionRepository
 * * <pre>
 *      *  * HMSET spring:session:sessions:33fdd1b6-b496-4b33-9f7d-df96679d32fe creationTime 1404360000000 maxInactiveInterval 1800 lastAccessedTime 1404360000000 sessionAttr:attrName someAttrValue sessionAttr2:attrName someAttrValue2
 *      *  * EXPIRE spring:session:sessions:33fdd1b6-b496-4b33-9f7d-df96679d32fe 2100
 *      *  * APPEND spring:session:sessions:expires:33fdd1b6-b496-4b33-9f7d-df96679d32fe ""
 *      *  * EXPIRE spring:session:sessions:expires:33fdd1b6-b496-4b33-9f7d-df96679d32fe 1800
 *      *  * SADD spring:session:expirations:1439245080000 expires:33fdd1b6-b496-4b33-9f7d-df96679d32fe
 *      *  * EXPIRE spring:session:expirations1439245080000 2100
 *      *  * </pre>
 *
 * @see com.ball.boss.config.HttpSessionConfig
 *       EnableRedisHttpSession.redisNamespace
 * @author littlehow
 */
public interface LoginService {
    String SPRING_SESSION_KEY_PREFIX = "ball:boss:token:sessions:";
    String SPRING_SESSION_EXPIRE_PREFIX = SPRING_SESSION_KEY_PREFIX + "expires:";
    /**
     * 用户登录
     * @param account   -  账户编号
     * @param password  -  密码
     * @param sessionId -  当前会话的session编号
     * @return - 用户信息
     */
    UserInfo login(String account, String password, String sessionId);

    /**
     * 退出登录
     * @param userId    -  用户编号
     */
    void logout(String userId);

    /**
     * 检查用户是否锁定
     * @param userId - 用户编号
     */
    void checkLocked(String userId);

    /**
     * 检查是否
     * @param sessionId - spring session id
     */
    void checkKickOut(String sessionId);
}
