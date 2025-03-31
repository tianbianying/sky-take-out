package com.sky.service;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.result.PageResult;
import com.sky.vo.DishVO;

import java.util.List;

/**
 * @description:
 * @author: Excell
 * @date: @data 2025年03月20日 09:25
 */
public interface DishService {
    /**
     * @description: 新增菜品
     * @title: saveDish
     * @param: [dishDTO]
     */
    void saveDish(DishDTO dishDTO);

    /**
     * @description: 获取菜品分页
     * @title: getList
     * @param: [dishPageQueryDTO]
     */
    PageResult getList(DishPageQueryDTO dishPageQueryDTO);

    /**
     * @description: 根据id批量删除菜品
     * @title: delDishes
     * @param: [ids]
     */
    void delDishes(List<Long> ids);

    /**
     * @description: 根据id获取菜品
     * @title: getDish
     * @param: [id]
     */
    DishVO getDish(Long id);

    /**
     * @description: 根据分类id获取菜品
     * @title: getDishByCategoryID
     * @param: []
     */
    List<Dish> getDishByCategoryID(Long categoryId);

    /**
     * @description: 修改菜品
     * @title: updateDish
     * @param: [dishDTO]
     */
    void updateDish(DishDTO dishDTO);

    /**
     * @description: 根据id修改菜品状态
     * @title: startOrStop
     * @param: [status, id]
     */
    void startOrStop(Integer status, Long id);

    /**
     * @description: 条件查询菜品和口味
     * @title: listWithFlavor
     * @param: [dish]
     */
    List<DishVO> listWithFlavor(Dish dish);
}











