package com.ball.proxy.controller.currency.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author fanyongpeng
 * @date 11/1/22
 **/
@Data
@ApiModel(description = "币种信息")
public class CurrencyResp {

    private Long id;

    @ApiModelProperty("币种编码")
    private String currencyCode;

    @ApiModelProperty("币种中文名称")
    private String description;

    /**
     * 对人民币对汇率 1 currency_code = rate RMB
     */
    @ApiModelProperty("对人民币对汇率 1 currency_code = rate RMB")
    private BigDecimal rate;

    private Integer status;

    private LocalDateTime updateTime;
}
