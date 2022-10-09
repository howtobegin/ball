package com.ball.boss.dao.ext;

import com.ball.boss.dao.entity.BossMenu;
import com.ball.boss.dao.entity.BossRole;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface PrivilegeMapper {

    List<BossMenu> queryMenuByRoleIds(@Param("roleIds") List<String> roleIds);

    List<BossRole> queryRoleByUserId(@Param("userId") String userId);

}
