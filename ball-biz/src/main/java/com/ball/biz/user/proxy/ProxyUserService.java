package com.ball.biz.user.proxy;

import com.ball.base.model.Const;
import com.ball.base.model.enums.YesOrNo;
import com.ball.base.transaction.TransactionSupport;
import com.ball.base.util.BizAssert;
import com.ball.base.util.PasswordUtil;
import com.ball.biz.base.service.IIdGenService;
import com.ball.biz.base.service.TableNameEnum;
import com.ball.biz.enums.UserTypeEnum;
import com.ball.biz.exception.BizErrCode;
import com.ball.biz.user.assist.LoginAssist;
import com.ball.biz.user.bo.ProxyRateInfo;
import com.ball.biz.user.entity.UserExtend;
import com.ball.biz.user.entity.UserInfo;
import com.ball.biz.user.entity.UserLoginSession;
import com.ball.biz.user.service.IUserExtendService;
import com.ball.biz.user.service.IUserInfoService;
import com.ball.biz.user.service.IUserLoginSessionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 代理
 * @author JimChery
 */
@Service
@Slf4j
public class ProxyUserService {
    @Autowired
    private IUserInfoService userInfoService;

    @Autowired
    private IIdGenService idGenService;

    @Autowired
    private TransactionSupport transactionSupport;

    @Autowired
    private IUserLoginSessionService userLoginSessionService;

    @Autowired
    private IUserExtendService userExtendService;

    @Autowired
    private LoginAssist loginAssist;

    private String SPRING_SESSION_KEY_PREFIX = "ball:proxy:token:sessions:";
    private String SPRING_SESSION_EXPIRE_PREFIX = SPRING_SESSION_KEY_PREFIX + "expires:";

    /**
     * 查询代理用户
     * @param userId - 用户编号
     * @return -
     */
    public UserInfo getByUid(Long userId) {
        UserInfo userInfo = userInfoService.lambdaQuery().eq(UserInfo::getId, userId).one();
        if (userInfo == null) {
            return null;
        }
        if (userInfo.getUserType().equals(UserTypeEnum.GENERAL.v)) {
            return null;
        }
        return userInfo;
    }

    /**
     * 添加登一
     * @param account   - 账号
     * @param userName  - 用户名
     * @param password  - 密码
     * @return
     */
    public Long addProxyOne(String account, String userName, String password, String balanceMode) {
        // 判断用户是否存在
        return addProxy0(account, userName, password, 0L,
                Const.SYSTEM_OPERATOR, UserTypeEnum.PROXY_ONE.v, null, balanceMode);
    }

    /**
     * 添加代理
     * @param account    - 新代理账号
     * @param userName   - 用户名
     * @param password   - 密码
     * @param proxyUid   - 代理用户编号
     * @return -
     */
    public Long addProxy(String account, String userName, String password, Long proxyUid, String balanceMode) {
        UserInfo proxy = getByUid(proxyUid);
        BizAssert.notNull(proxy, BizErrCode.USER_NOT_EXISTS);
        UserTypeEnum typeEnum = UserTypeEnum.proxyOf(proxy.getUserType());
        BizAssert.isTrue(typeEnum.createProxy, BizErrCode.DATA_ERROR);
        String proxyInfo = proxy.getProxyInfo();
        if (proxyInfo == null) {
            proxyInfo = proxyUid.toString();
        } else {
            proxyInfo = proxyInfo + Const.RELATION_SPLIT + proxyUid;
        }
        return addProxy0(account, userName, password, proxyUid, proxy.getAccount(), typeEnum.next, proxyInfo, balanceMode);
    }

    /**
     * 代理用户登录
     * @param sessionId   -
     * @param account     - 账号
     * @param password    - 密码
     * @param userType    - 用户类型 {@link UserTypeEnum}
     * @return -
     */
    public UserInfo login(String sessionId, String account, String password, Integer userType) {
        return loginAssist.login(sessionId, account, password, userType, SPRING_SESSION_KEY_PREFIX, SPRING_SESSION_EXPIRE_PREFIX);
    }

    /**
     * 代理用户登出
     * @param userId -
     */
    public void logout(Long userId) {
        loginAssist.logout(userId, SPRING_SESSION_KEY_PREFIX, SPRING_SESSION_EXPIRE_PREFIX);
    }

    /**
     * 根据会员获取会员上的代理1-3的分成比例
     * @param userId  - 会员编号
     * @return -
     */
    public ProxyRateInfo getProxyRateByUid(Long userId) {
        ProxyRateInfo proxyRateInfo = new ProxyRateInfo();
        UserInfo userInfo = userInfoService.getByUid(userId);
        BizAssert.isTrue(UserTypeEnum.GENERAL.isMe(userInfo.getUserType()), BizErrCode.DATA_ERROR);
        List<Long> uid = getUidByProxyInfo(userInfo.getProxyInfo());
        Map<Integer, BigDecimal> percentMap = userExtendService.getByUid(uid)
                .stream().collect(Collectors.toMap(UserExtend::getUserType, UserExtend::getProxyRate));
        // 首先获取三级
        BigDecimal three = percentMap.get(UserTypeEnum.PROXY_THREE.v);
        BigDecimal two = percentMap.get(UserTypeEnum.PROXY_TWO.v);
        BigDecimal one = percentMap.get(UserTypeEnum.PROXY_ONE.v);
        proxyRateInfo.setProxyThreeRate(three);
        // 二级占比 = 自己的占比 - 三级占比
        proxyRateInfo.setProxyTwoRate(two.subtract(three));
        // 一级占比 = 自己的占比 - 二级占比
        proxyRateInfo.setProxyOneRate(one.subtract(two));
        return proxyRateInfo;
    }

    private Long addProxy0(String account, String userName, String password, Long proxyUid,
                           String proxyAccount, Integer userType, String proxyInfo,
                           String balanceMode) {
        // 判断用户是否存在
        UserInfo userInfo = userInfoService.lambdaQuery().eq(UserInfo::getAccount, account)
                .eq(UserInfo::getUserType, UserTypeEnum.PROXY_ONE.v).one();
        BizAssert.isNull(userInfo, BizErrCode.USER_EXISTS);
        Long userId = idGenService.get(TableNameEnum.USER_INFO);
        transactionSupport.execute(() -> {
            userInfoService.save(new UserInfo().setId(userId)
                    .setAccount(account).setChangePasswordFlag(YesOrNo.NO.v)
                    .setLoginAccount(account).setPassword(PasswordUtil.get(password))
                    .setProxyAccount(proxyAccount).setStatus(YesOrNo.YES.v)
                    .setUserName(userName).setUserType(userType)
                    .setProxyUserId(proxyUid).setProxyInfo(proxyInfo)
                    .setBalanceMode(balanceMode).setChangePasswordTime(0L)
            );
            userLoginSessionService.save(new UserLoginSession()
                    .setUserId(userId).setSessionId(Const.SESSION_DEFAULT)
            );
        });
        return userId;
    }

    private List<Long> getUidByProxyInfo(String proxyInfo) {
        String[] uids = proxyInfo.split(Const.RELATION_SPLIT);
        List<Long> uid = new ArrayList<>();
        for (String s : uids) {
            uid.add(Long.valueOf(s));
        }
        return uid;
    }
}
