package com.ball.biz.match.entity;

import com.ball.base.model.Paging;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * <p>
 * 收藏夹表
 * </p>
 *
 * @author lhl
 * @since 2022-10-28
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Favorite extends Paging {

    /**
     * 自增编号
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 用户编号
     */
    private Long userId;

    /**
     * 联赛ID
     */
    private String leagueId;

    /**
     * 比赛ID
     */
    private String matchId;

    /**
     * 收藏类型：1 滚球；2 今日；3 早盘
     */
    private Integer favoriteType;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;


}
