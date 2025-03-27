package com.sky.dto;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;


@Data
@ApiModel("前端进行分页查询时传输的数据")
public class EmployeePageQueryDTO implements Serializable {

    // 员工姓名
    private String name;

    // 页码
    private int page;

    // 每页显示记录数
    private int pageSize;

}
