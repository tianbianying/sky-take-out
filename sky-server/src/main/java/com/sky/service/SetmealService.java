package com.sky.service;

import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.result.PageResult;
import com.sky.vo.DishItemVO;
import com.sky.vo.SetmealVO;

import java.util.List;

/**
 * @description: 套餐类服务层的接口
 * @author: Excell
 * @date: 2025年03月21日 15:46
 */
public interface SetmealService {
    /**
     * @description: 增加新的套餐
     * @title: saveSetmeal
     * @param: [setmealVO]
     */
    void saveSetmeal(SetmealVO setmealVO);

    /**
     * @description: 获取分页列表
     * @title: pageList
     * @param: [setmealPageQueryDTO]
     */
    PageResult pageList(SetmealPageQueryDTO setmealPageQueryDTO);

    /**
     * @description: 删除套餐
     * @title: delSetmeal
     * @param: [id]
     */
    void delSetmeal(List<Long> id);

    /**
     * @description: 根据id进行套餐的修改
     * @title: updateSetmeal
     * @param: [setmealVO]
     */
    void updateSetmeal(SetmealVO setmealVO);

    /**
     * @description: 根据id获取套餐信息
     * @title: getSetmealById
     * @param: [id]
     */
    Setmeal getSetmealById(Long id);

    /**
     * @description: 修改起售停售状态
     * @title: startOrStop
     * @param: [status]
     */
    void startOrStop(Integer status, Long id);

    /**
     * @description: 条件查询
     * @title: list
     * @param: [setmeal]
     */
    List<Setmeal> list(Setmeal setmeal);

    /**
     * @description: 根据id查询菜品选项
     * @title: getDishItemById
     * @param: [id]
     */
    List<DishItemVO> getDishItemById(Long id);
}
