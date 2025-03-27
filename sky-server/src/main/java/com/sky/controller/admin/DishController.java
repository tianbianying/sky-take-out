package com.sky.controller.admin;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.ibatis.annotations.Delete;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @description: 菜品相关接口
 * @author: Excell
 * @data 2025年03月20日 09:20
 */
@RestController
@RequestMapping("/admin/dish")
@Api(tags = "菜品相关接口")
@Validated
public class DishController {
    @Resource
    private DishService dishService;

    /**
     * @description: 新增菜品
     * @title: saveDishWithFlavors
     * @param: [dishDTO]
     */
    @ApiOperation("新增菜品")
    @PostMapping
    public Result saveDishWithFlavors(@RequestBody @Validated(DishDTO.Add.class) DishDTO dishDTO) {
        dishService.saveDish(dishDTO);
        return Result.success();
    }

    /**
     * @description: 菜品分页
     * @title: pageList
     * @param: [dishPageQueryDTO]
     */
    @GetMapping("/page")
    @ApiOperation("菜品分页查询")
    public Result<PageResult> pageList(DishPageQueryDTO dishPageQueryDTO) {
        return Result.success(dishService.getList(dishPageQueryDTO));
    }

    /**
     * @description: 批量删除
     * @title: delDishes
     * @param: [ids]
     */
    @DeleteMapping
    @ApiOperation("批量删除")
    public Result delDishes(@RequestParam List<Long> ids) {
        dishService.delDishes(ids);
        return Result.success();
    }

    /**
     * @description: 根据id查询菜品
     * @title: getDish
     * @param: [id]
     */
    @GetMapping("/{id}")
    @ApiOperation("根据id查询菜品")
    public Result<DishVO> getDish(@PathVariable Long id) {
        return Result.success(dishService.getDish(id));
    }

    /**
     * @description: 根据id修改菜品
     * @title: updateDish
     * @param: [dishDTO]
     */
    @PutMapping
    @ApiOperation("根据id修改菜品")
    public Result updateDish(@RequestBody DishDTO dishDTO) {
        dishService.updateDish(dishDTO);
        return Result.success();
    }

    /**
     * @description: 根据分类id查询菜品
     * @title: getDishByCategoryId
     * @param: [categoryId]
     */
    @GetMapping("/list")
    @ApiOperation("根据分类id查询菜品")
    public Result<List<Dish>> getDishByCategoryId(Long categoryId) {
        return Result.success(dishService.getDishByCategoryID(categoryId));
    }

    /**
     * @description: 根据id改变菜品状态
     * @title: startOrStop
     * @param: [status, id]
     */
    @PostMapping("/status/{status}")
    @ApiOperation("改变菜品状态")
    public Result startOrStop(@PathVariable Integer status, Long id) {
        dishService.startOrStop(status, id);
        return Result.success();
    }

}















