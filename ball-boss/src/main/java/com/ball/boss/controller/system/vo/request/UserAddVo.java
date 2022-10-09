package com.ball.boss.controller.system.vo.request;

import com.ball.boss.controller.system.vo.UserBaseVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;


@Data
@ApiModel(description = "用户添加信息")
public class UserAddVo extends UserBaseVo {

    @ApiModelProperty("密码(长度在4-18位)")
    @NotBlank(message = "密码不能为空")
    @Length(min = 4, max = 18, message = "密码长度为4-18位")
    private String password;

}

