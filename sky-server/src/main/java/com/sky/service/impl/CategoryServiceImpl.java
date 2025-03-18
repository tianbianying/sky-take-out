package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import com.sky.mapper.CategoryMapper;
import com.sky.result.PageResult;
import com.sky.service.CategoryService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @description: 用户分类的服务层实现类
 * @author: Excell
 * @data 2025年03月18日 19:03
 */
@Service
public class CategoryServiceImpl implements CategoryService {
    @Resource
    private CategoryMapper categoryMapper;

    /**
     * @param categoryPageQueryDTO
     * @description: 获取分类列表
     * @title: getList
     * @param: []
     */
    @Override
    public PageResult getList(CategoryPageQueryDTO categoryPageQueryDTO) {

        PageHelper.startPage(categoryPageQueryDTO.getPage(), categoryPageQueryDTO.getPageSize());

        Page<Category> categories = categoryMapper.getList(categoryPageQueryDTO);

        PageResult pageResult = new PageResult(categories.getTotal(), categories.getResult());

        return pageResult;
    }
}







