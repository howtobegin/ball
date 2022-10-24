package com.ball.biz.match.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import com.ball.base.model.Paging;

/**
 * <p>
 * 
 * </p>
 *
 * @author lhl
 * @since 2022-10-19
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("ds_teams")
public class Teams extends Paging {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private String teamId;

    private String leagueId;

    private String name;

    /**
     * BetTeam logo url.The picture is saved locally for use, please do not call it directly.
     */
    private String logo;

    /**
     * e.g. 1984 or 1890-9-6
     */
    private String foundingDate;

    /**
     * e.g. Avda. Aristides Maillol s/n，ES-08028 BARCELONA
     */
    private String address;

    /**
     * e.g. Barcelona
     */
    private String area;

    /**
     * e.g. Camp Nou
     */
    private String venue;

    /**
     * e.g. 99354
     */
    private Integer capacity;

    /**
     * e.g. Txingurri Valverde
     */
    private String coach;

    /**
     * Official website
     */
    private String website;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 最后一次更新时间
     */
    private LocalDateTime updateTime;


}
