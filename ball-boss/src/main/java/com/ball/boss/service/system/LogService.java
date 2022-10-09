package com.ball.boss.service.system;






import com.ball.boss.dao.entity.BossCheckLog;
import com.ball.boss.dao.entity.BossOperationLog;

import java.util.List;
import java.util.Map;

public interface LogService {
    /**
     * 查询业务操作日志
     *
     * @param bizId   -- 业务编号
     * @param bizType -- 业务类型
     * @return
     */
    List<BossOperationLog> queryLogByBizId(String bizId, String bizType);

    /**
     * 查询审核日志
     *
     * @param bizId   -- 业务编号
     * @param bizCode -- 业务代码
     * @return
     */
    List<BossCheckLog> queryCheckLogByBizId(String bizId, String bizCode);

    /**
     * 根据多个业务编号获取审核日志信息
     *
     * @param bizIds  -- 业务编号集合
     * @param bizCode -- 业务代码
     * @return
     */
    Map<String, List<BossCheckLog>> queryCheckLogByBizIds(List<String> bizIds, String bizCode);
}
