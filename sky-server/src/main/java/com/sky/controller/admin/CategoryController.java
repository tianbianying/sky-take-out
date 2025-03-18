package com.sky.controller.admin;

import com.sky.dto.CategoryPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.CategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @description: 分类管理
 * @author: Excell
 * @data 2025年03月18日 18:55
 */
@RestController
@RequestMapping("/admin/category")
@Api(tags = "菜品分类相关接口")
public class CategoryController {
    @Resource
    private CategoryService categoryService;

    /**
     * @description: 分类数据分页
     * @title: getCategoryList
     * @param: []
     */
    @GetMapping("/page")
    @ApiOperation("获取分类分页")
    public Result<PageResult> getCategoryList(@Validated CategoryPageQueryDTO categoryPageQueryDTO) {
        return Result.success(categoryService.getList(categoryPageQueryDTO));
    }
}



