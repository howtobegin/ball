package com.ball.biz.bet.processor.cache;

import com.ball.biz.user.entity.UserInfo;
import com.ball.biz.user.service.IUserInfoService;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author lhl
 * @date 2022/10/25 下午6:53
 */
@Slf4j
@Component
public class UserProxyCache implements InitializingBean {
    @Autowired
    private IUserInfoService userInfoService;

    private static final Map<Long, Long> cache = Maps.newConcurrentMap();

    public void load() {
        List<UserInfo> list = userInfoService.list();
        Map<Long, Long> collect = list.stream().collect(Collectors.toMap(UserInfo::getId, UserInfo::getProxyUserId));
        cache.putAll(collect);
    }

    public Long getProxy(Long userId) {
        if (userId == null) {
            return null;
        }
        Long proxyUserId = cache.get(userId);
        if (proxyUserId == null) {
            synchronized (userId) {
                if ((proxyUserId = cache.get(userId)) == null) {
                    UserInfo user = userInfoService.getByUid(userId);
                    proxyUserId = Optional.ofNullable(user).map(UserInfo::getProxyUserId).orElse(null);
                    if (proxyUserId != null) {
                        cache.put(userId, proxyUserId);
                    }
                }
            }
        }
        return proxyUserId;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        load();
    }
}
