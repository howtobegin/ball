package com.ball.boss.dao.ext;


import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;

@Mapper
public interface KickLogMapper {
    /**
     * 查询指定时间内用户的踢出数量
     *
     * @param userId   - 用户编号
     * @param kickTime - 时间(踢出时间>=kickTime)
     * @return
     */
    int countKickOut(@Param("userId") String userId,
                     @Param("kickTime") Date kickTime);

    /**
     * 锁定用户
     * @param userId - 用户编号
     * @return -
     */
    int lockUser(@Param("userId") String userId);
}
