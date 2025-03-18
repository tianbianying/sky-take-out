package com.sky.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.groups.Default;
import java.io.Serializable;


@Data
public class CategoryDTO implements Serializable {

    // 主键
    @NotNull(groups = {Update.class})
    private Long id;

    // 类型 1 菜品分类 2 套餐分类
    @NotNull
    private Integer type;

    // 分类名称
    @NotNull
    private String name;

    // 排序
    @NotNull
    private Integer sort;

    public interface Update extends Default {
    }

    public interface Add extends Default {

    }

}
