package com.sky.controller.admin;

import com.sky.dto.CategoryDTO;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.CategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @description: 分类管理
 * @author: Excell
 * @data 2025年03月18日 18:55
 */
@RestController("adminCategoryController")
@RequestMapping("/admin/category")
@Api(tags = "菜品分类相关接口")
@Validated
@Slf4j
public class CategoryController {
    @Autowired
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

    /**
     * @description: 根据类型查询分类
     * @title: getCategoryByType
     * @param: [type]
     */
    @GetMapping("/list")
    @ApiOperation("根据类型获取分类")
    public Result<List> getCategoryByType(Integer type) {
        return Result.success(categoryService.getCategoryByType(type));
    }

    /**
     * @description: 新增分类
     * @title: save
     * @param: [categoryDTO]
     */
    @PostMapping
    @ApiOperation("新增菜品分类")
    public Result save(@RequestBody @Validated(CategoryDTO.Add.class) CategoryDTO categoryDTO) {
        categoryService.save(categoryDTO);
        return Result.success();
    }

    /**
     * @description: 根据id删除分类
     * @title: delById
     * @param: [id]
     */
    @DeleteMapping
    @ApiOperation("根据id删除分类")
    public Result delById(Long id) {
        if (id == null) {
            return Result.error("id不能为空");
        }
        categoryService.delById(id);
        return Result.success();
    }

    /**
     * @description: 开启或关闭某个分类
     * @title: startOrStop
     * @param: [status, id]
     */
    @PostMapping("/status/{status}")
    @ApiOperation("开启或关闭某个分类")
    public Result startOrStop(@PathVariable Integer status, Long id) {
        categoryService.startOrStop(status, id);
        return Result.success();
    }

    /**
     * @description: 修改分类
     * @title: update
     * @param: [categoryDTO]
     */
    @PutMapping
    @ApiOperation("修改分类")
    public Result update(@Validated({CategoryDTO.Update.class}) @RequestBody CategoryDTO categoryDTO) {
        categoryService.update(categoryDTO);
        return Result.success();
    }

}



