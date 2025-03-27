package com.sky.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Pattern;
import java.io.Serializable;

@Data
@ApiModel("接收前端用户数据的类")
public class EmployeeDTO implements Serializable {

    @ApiModelProperty("id")
    private Long id;
    @ApiModelProperty("用户名")
    private String username;
    @ApiModelProperty("姓名")
    private String name;
    @ApiModelProperty("手机号")
    @Pattern(regexp = "/^(?:(?:\\+|00)86)?1\\d{10}$/")
    private String phone;
    @ApiModelProperty("性别")
    private String sex;
    @ApiModelProperty("身份证号")
    @Pattern(regexp = "/^[1-9]\\d{5}(?:18|19|20)\\d{2}(?:0[1-9]|10|11|12)(?:0[1-9]|[1-2]\\d|30|31)\\d{3}[\\dXx]$/")
    private String idNumber;

}
