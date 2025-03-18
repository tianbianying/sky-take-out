package com.sky.service;

import com.sky.dto.CategoryPageQueryDTO;
import com.sky.result.PageResult;

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
}
