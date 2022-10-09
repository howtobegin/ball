package com.ball.boss.dao.ext;


import com.ball.boss.dao.entity.BossCheckLog;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CheckLogMapper {
    /**
     * 查询多个业务的审核日志
     *
     * @param bizCode
     * @param bizIds
     * @return
     */
    List<BossCheckLog> selectByBizIds(@Param("bizCode") String bizCode,
                                      @Param("bizIds") List<String> bizIds);
}
