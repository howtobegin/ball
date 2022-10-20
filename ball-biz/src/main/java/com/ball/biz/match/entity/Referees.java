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
@TableName("ds_referees")
public class Referees extends Paging {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private String matchId;

    /**
     * 1: Referee 2: Assistant referee 3: Fourth official
     */
    private Boolean typeId;

    private Integer refereeId;

    private String name;

    /**
     * e.g. 1979/1/22 0:00:00
     */
    private String birthday;

    private String country;

    /**
     * Referee photo url. The picture is saved locally for use, please do not call it directly.
     */
    private String photo;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 最后一次更新时间
     */
    private LocalDateTime updateTime;


}
