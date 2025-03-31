package com.sky.controller.admin;

import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.SetmealService;
import com.sky.vo.SetmealVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @description: 套餐类的控制层
 * @author: Excell
 * @data 2025年03月21日 15:46
 */
@RestController("adminSetmealController")
@RequestMapping("/admin/setmeal")
@Api(tags = "套餐相关接口")
@Validated
@Slf4j
public class SetmealController {
    @Autowired
    private SetmealService setmealService;

    /**
     * @description: 新增套餐
     * @title: saveSetmeal
     * @param: [setmealVO]
     */
    @PostMapping
    @ApiOperation("增加新的套餐")
    @CachePut(cacheNames = "setmeal", key = "#setmealVO.categoryId")
    public Result saveSetmeal(@RequestBody @Validated SetmealVO setmealVO) {
        setmealService.saveSetmeal(setmealVO);
        return Result.success();
    }

    /**
     * @description: 分页查询
     * @title: pageList
     * @param: [setmealPageQueryDTO]
     */
    @GetMapping("/page")
    @ApiOperation("根据条件分页查询")
    public Result<PageResult> pageList(SetmealPageQueryDTO setmealPageQueryDTO) {
        return Result.success(setmealService.pageList(setmealPageQueryDTO));
    }

    /**
     * @description: 根据id删除套餐
     * @title: delSetmeal
     * @param: [ids]
     */
    @DeleteMapping
    @ApiOperation("根据id删除套餐")
    @CacheEvict(cacheNames = "setmeal", allEntries = true)
    public Result delSetmeal(@RequestParam List<Long> ids) {
        setmealService.delSetmeal(ids);
        return Result.success();
    }

    /**
     * @description: 通过id修改套餐
     * @title: updateSetmeal
     * @param: [setmealVO]
     */
    @PutMapping
    @ApiOperation("进行套餐数据的修改")
    @CacheEvict(cacheNames = "setmeal", allEntries = true)
    public Result updateSetmeal(@RequestBody SetmealVO setmealVO) {
        setmealService.updateSetmeal(setmealVO);
        return Result.success();
    }

    /**
     * @description: 根据id获取套餐
     * @title: getSetmealById
     * @param: [id]
     */
    @GetMapping("/{id}")
    @ApiOperation("根据id获取套餐数据")
    public Result<Setmeal> getSetmealById(@PathVariable Long id) {
        return Result.success(setmealService.getSetmealById(id));
    }

    /**
     * @description: 根据id修改起售停售的状态
     * @title: startOrStop
     * @param: [status, id]
     */
    @PostMapping("/status/{status}")
    @ApiOperation("根据id修改状态")
    @CacheEvict(cacheNames = "setmeal", allEntries = true)
    public Result startOrStop(@PathVariable Integer status, Long id) {
        setmealService.startOrStop(status, id);
        return Result.success();
    }

}













