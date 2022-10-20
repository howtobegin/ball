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
@TableName("ds_players")
public class Players extends Paging {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private String recordId;

    private String playerId;

    private String name;

    /**
     * e.g. 1987-02-02
     */
    private String birthday;

    /**
     * Unit: cm, e.g. 192
     */
    private Integer height;

    /**
     * e.g. Spain
     */
    private String country;

    /**
     * Left or Right
     */
    private String feet;

    /**
     * Unit: kg, e.g. 77
     */
    private Integer weight;

    /**
     * Player photo url. The picture is saved locally for use, please do not call it directly.
     */
    private String photo;

    /**
     * Unit: ten thousand pounds, e.g. 3150
     */
    private String value;

    private String teamId;

    /**
     * e.g. Centre Back
     */
    private String position;

    private Integer number;

    private String introduce;

    /**
     * When the contract ends, e.g. 2022-06-30
     */
    private String contractEndDate;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 最后一次更新时间
     */
    private LocalDateTime updateTime;


}
