package com.ball.biz.user.mapper.ext;

import com.ball.biz.user.bo.ProxyStatistics;
import com.ball.biz.user.bo.ProxyUserInfo;
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

    ProxyStatistics selectProxyStatistics(@Param("proxyUid") Long proxyUid);

    Integer selectProxyStatistics(@Param("start") LocalDateTime start,
                                  @Param("end") LocalDateTime end);
}
