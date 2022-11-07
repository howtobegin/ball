package com.ball.biz.user.mapper.ext;

import com.ball.biz.user.bo.ProxyChildrenBalance;
import com.ball.biz.user.bo.ProxyStatistics;
import com.ball.biz.user.bo.ProxyUserInfo;
import com.ball.biz.user.bo.UserStatusBO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author littlehow
 */
@Mapper
public interface UserExtMapper {
    List<ProxyUserInfo> proxyUser(@Param("proxyUids") List<Long> proxyUids);

    List<ProxyStatistics> selectProxyStatistics(@Param("proxyUid") Long proxyUid);

    Integer selectProxyStatisticsPeriod(@Param("proxyUid") Long proxyUid,
                                  @Param("start") LocalDateTime start,
                                  @Param("end") LocalDateTime end);

    List<ProxyChildrenBalance> selectProxyChildrenBalance(@Param("proxyUid") Long proxyUid);

    UserStatusBO selectStatusByType(@Param("userType") Integer userType,
                                    @Param("proxyInfo") String proxyInfo);
}
