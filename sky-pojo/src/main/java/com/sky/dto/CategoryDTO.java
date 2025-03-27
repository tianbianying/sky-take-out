package com.sky.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.groups.Default;
import java.io.Serializable;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDTO implements Serializable {

    // 主键
    @NotNull(groups = {Update.class}, message = "ID不能为空")
    private Long id;

    // 类型 1 菜品分类 2 套餐分类
    @NotNull(message = "type不能为空")
    private Integer type;

    // 分类名称
    @NotBlank(message = "name不能为空")
    private String name;

    // 排序
    @NotNull(message = "sort不能为空")
    private Integer sort;

    public interface Update extends Default {
    }

    public interface Add extends Default {

    }

}
