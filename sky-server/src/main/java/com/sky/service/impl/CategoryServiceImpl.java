package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.JwtClaimsConstant;
import com.sky.dto.CategoryDTO;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import com.sky.mapper.CategoryMapper;
import com.sky.result.PageResult;
import com.sky.service.CategoryService;
import com.sky.utils.ThreadLocalUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.Map;

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

    /**
     * @description: 创建新的菜品分类
     * @title: save
     * @param: [categoryDTO]
     */
    @Override
    public void save(CategoryDTO categoryDTO) {
        Category category = new Category();

        // 拷贝
        BeanUtils.copyProperties(categoryDTO, category);

        // 为新的分类对象赋值
        category.setStatus(0);
        category.setCreateTime(LocalDateTime.now());
        category.setUpdateTime(LocalDateTime.now());

        Map map = (Map) ThreadLocalUtil.get();

        category.setCreateUser(Long.valueOf((Integer) map.get(JwtClaimsConstant.EMP_ID)));
        category.setUpdateUser(Long.valueOf((Integer) map.get(JwtClaimsConstant.EMP_ID)));

        categoryMapper.addCategory(category);
    }

    /**
     * @description: 根据id删除分类
     * @title: delById
     * @param: [id]
     */
    @Override
    public void delById(Long id) {
        categoryMapper.delById(id);
    }

    /**
     * @description: 根据id修改分类状态
     * @title: startOrStop
     * @param: [status, id]
     */
    @Override
    public void startOrStop(Integer status, Long id) {
        Category category = Category.builder()
                .id(id)
                .status(status)
                .build();
        categoryMapper.update(category);
    }

    /**
     * @description: 修改分类
     * @title: update
     * @param: [categoryDTO]
     */
    @Override
    public void update(CategoryDTO categoryDTO) {
        Category category = new Category();
        BeanUtils.copyProperties(categoryDTO, category);
        Map map = (Map) ThreadLocalUtil.get();

        category.setUpdateTime(LocalDateTime.now());
        category.setUpdateUser(Long.valueOf((Integer) map.get(JwtClaimsConstant.EMP_ID)));

        categoryMapper.update(category);
    }
}







