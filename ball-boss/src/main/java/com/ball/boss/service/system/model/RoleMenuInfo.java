package com.ball.boss.service.system.model;

import com.ball.boss.dao.entity.BossMenu;
import com.ball.boss.dao.entity.BossRole;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class RoleMenuInfo {
    private List<BossRole> roles;
    private List<BossMenu> menus;
}
