package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import org.apache.ibatis.annotations.Mapper;

/**
 * @description: 用户分类的持久层
 * @author: Excell
 * @data 2025年03月18日 19:04
 */
@Mapper
public interface CategoryMapper {
    /**
     * @description: 根据条件查询列表
     * @title: getList
     * @param: [categoryPageQueryDTO]
     */
    Page<Category> getList(CategoryPageQueryDTO categoryPageQueryDTO);
}
