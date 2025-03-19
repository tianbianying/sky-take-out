package com.sky.service;

import com.sky.dto.CategoryDTO;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.result.PageResult;

import java.util.List;

/**
 * @description: 用户分类的服务层接口
 * @author: Excell
 * @date: @data 2025年03月18日 19:03
 */
public interface CategoryService {
    /**
     * @param categoryPageQueryDTO
     * @description: 获取分类列表
     * @title: getList
     * @param: []
     */
    PageResult getList(CategoryPageQueryDTO categoryPageQueryDTO);

    /**
     * @description: 添加新的菜品分类
     * @title: save
     * @param: [categoryDTO]
     */
    void save(CategoryDTO categoryDTO);

    /**
     * @description: 根据id删除菜品分类
     * @title: delById
     * @param: [id]
     */
    void delById(Long id);

    /**
     * @description: 根据id修改分类状态
     * @title: startOrStop
     * @param: [status, id]
     */
    void startOrStop(Integer status, Long id);

    /**
     * @description: 修改分类
     * @title: update
     * @param: [categoryDTO]
     */
    void update(CategoryDTO categoryDTO);

    /**
     * @description:
     * @title: getCategoryByType
     * @param: [type]
     */
    List getCategoryByType(Integer type);
}
