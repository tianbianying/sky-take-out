package com.sky.dto;

import com.sky.entity.DishFlavor;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.groups.Default;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
public class DishDTO implements Serializable {

    private Long id;
    // 菜品名称
    @NotBlank
    private String name;
    // 菜品分类id
    @NotNull
    private Long categoryId;
    // 菜品价格
    @NotNull
    private BigDecimal price;
    // 图片
    @NotBlank
    private String image;
    // 描述信息
    private String description;
    // 0 停售 1 起售
    private Integer status;
    // 口味
    private List<DishFlavor> flavors = new ArrayList<>();

    public interface Add extends Default {
    }

}
