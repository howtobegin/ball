package com.ball.biz.user.proxy;

import com.ball.biz.enums.UserTypeEnum;
import com.ball.biz.user.entity.UserInfo;
import com.ball.biz.user.service.IUserInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 代理
 * @author littlehow
 */
@Service
@Slf4j
public class ProxyUserService {
    @Autowired
    private IUserInfoService userInfoService;

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

//    public void addProxyOne(String )
}
