package com.sky.mapper;

import com.sky.entity.SetmealDish;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @description: 这是菜品套餐关联表
 * @author: Excell
 * @date: @data 2025年03月20日 20:06
 */
@Mapper
public interface SetmealDishMapper {
    /**
     * @description: 根据菜品id获取套餐id
     * @title: getSetmealById
     * @param: [ids]
     */
    List<Long> getSetmealById(List<Long> ids);

    /**
     * @description: 增加新的套餐与菜品关系
     * @title: insert
     * @param: [setmealDishes]
     */
    void insert(List<SetmealDish> setmealDishes);

    /**
     * @description: 根据套餐id删除套餐菜品表中的数据
     * @title: delSetmealDish
     * @param: [setmealId]
     */
    void delSetmealDish(List<Long> setmealIds);
}
