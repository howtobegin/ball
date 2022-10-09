package com.ball.boss.service.system;




import com.ball.base.model.PageResult;
import com.ball.boss.dao.entity.BossCode;

import java.util.List;

public interface CodeService {
    /**
     * 新增字典信息
     *
     * @param code -- 字典信息
     * @return -- 字典编号
     */
    String add(BossCode code);

    /**
     * 修改字典信息
     * 不可编辑状态的字典是不可修改的
     *
     * @param code -- 字典信息
     */
    void update(BossCode code);

    /**
     * 删除字典信息
     * 不可编辑状态的字典是不可删除的
     *
     * @param codeId -- 字典编号
     */
    void delete(String codeId);

    /**
     * 根据字典编号查询字典信息
     *
     * @param codeId -- 字典编号
     * @return -- 对应的字典信息
     */
    BossCode queryById(String codeId);

    /**
     * 根据字典对照字段查询字典信息
     *
     * @param field -- 对照字段
     * @return -- 对应的字典信息
     */
    List<BossCode> queryByField(String field);

    /**
     * 查询所有的字典信息
     *
     * @return -- 所有字典信息
     */
    List<BossCode> queryAll();

    /**
     * 分页查询字典信息，目前支持条件为field
     *
     * @param code -- 字典分页查询请求信息
     * @return -- 对应的分页信息
     */
    PageResult<BossCode> queryPaging(BossCode code);
}
