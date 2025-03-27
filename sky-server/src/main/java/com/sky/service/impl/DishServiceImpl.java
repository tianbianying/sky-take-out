package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.mapper.DishMapper;
import com.sky.mapper.FlavorMapper;
import com.sky.mapper.SetmealDishMapper;
import com.sky.result.PageResult;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @description:
 * @author: Excell
 * @data 2025年03月20日 09:25
 */
@Service
public class DishServiceImpl implements DishService {
    @Resource
    private DishMapper dishMapper;
    @Resource
    private FlavorMapper flavorMapper;
    @Resource
    private SetmealDishMapper setmealDishMapper;

    /**
     * @description: 新增菜品及口味
     * @title: saveDish
     * @param: [dishDTO]
     */
    @Transactional
    @Override
    public void saveDish(DishDTO dishDTO) {
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO, dish);
        dish.setStatus(StatusConstant.ENABLE);
        dishMapper.insert(dish);
        List<DishFlavor> flavors = dishDTO.getFlavors();
        if (flavors == null || flavors.isEmpty()) {
            return;
        }
        flavors.forEach(flavor -> {
            flavor.setDishId(dish.getId());
        });

        flavorMapper.insert(flavors);
    }

    /**
     * @description: 菜品分页
     * @title: getList
     * @param: [dishPageQueryDTO]
     */
    @Override
    public PageResult getList(DishPageQueryDTO dishPageQueryDTO) {
        PageHelper.startPage(dishPageQueryDTO.getPage(), dishPageQueryDTO.getPageSize());

        Page<DishVO> page = dishMapper.list(dishPageQueryDTO);

        return new PageResult(page.getTotal(), page.getResult());
    }

    /**
     * @description: 根据id批量进行删除
     * @title: delDishes
     * @param: [ids]
     */
    @Transactional
    @Override
    public void delDishes(List<Long> ids) {
        // 1.判断菜品是否停售
        List<Integer> status = dishMapper.getStatus(ids);

        status.forEach(s -> {
            if (s.equals(StatusConstant.ENABLE)) {
                throw new DeletionNotAllowedException(MessageConstant.SETMEAL_ON_SALE);
            }
        });


        // 2.判断菜品是否关联套餐
        List<Long> setmealIds = setmealDishMapper.getSetmealById(ids);
        if (setmealIds != null && setmealIds.size() > 0) {
            throw new DeletionNotAllowedException(MessageConstant.DISH_BE_RELATED_BY_SETMEAL);
        }


        // 3.不关联套餐也停售了就可以进行删除了
        // 3.1删除口味表,根据菜品id
        flavorMapper.delByIds(ids);
        // 3.2删除菜品
        dishMapper.delByIds(ids);
    }

    /**
     * @description: 根据id获取菜品
     * @title: getDish
     * @param: [id]
     */
    @Transactional
    @Override
    public DishVO getDish(Long id) {
        // 创建DishVO对象
        DishVO dishVO = new DishVO();

        // 根据菜品id获取口味
        List<DishFlavor> dishFlavors = flavorMapper.getById(id);

        // 根据id获取菜品对象
        Dish dish = dishMapper.getDishById(id);

        BeanUtils.copyProperties(dish, dishVO);
        dishVO.setFlavors(dishFlavors);

        return dishVO;
    }

    /**
     * @description: 根据分类id获取菜品
     * @title: getDishByCategoryID
     * @param: [categoryId]
     */
    @Override
    public List<Dish> getDishByCategoryID(Long categoryId) {
        return dishMapper.getDishByCategoryId(categoryId);
    }

    /**
     * @description: 修改菜品
     * @title: updateDish
     * @param: [dishDTO]
     */
    @Transactional
    @Override
    public void updateDish(DishDTO dishDTO) {
        // 获取口味
        List<DishFlavor> flavors = dishDTO.getFlavors();

        // 创建dish对象
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO, dish);

        // 1.修改口味表
        // 1.1根据菜品id删除之前的口味
        ArrayList<Long> idList = new ArrayList<>();
        idList.add(dishDTO.getId());
        flavorMapper.delByIds(idList);
        // 1.2根据菜品id增加新的口味
        // 如果新增的口味为空
        if (!(flavors != null && flavors.size() > 0)) {
            dishMapper.update(dish);
            return;
        }
        // 为口味表的菜品id赋值
        flavors.forEach(f -> {
            f.setDishId(dishDTO.getId());
        });

        flavorMapper.insert(flavors);

        // 2.修改菜品表
        dishMapper.update(dish);
    }

    /**
     * @description: 根据id修改菜品状态
     * @title: startOrStop
     * @param: [status, id]
     */
    @Override
    public void startOrStop(Integer status, Long id) {
        Dish dish = Dish.builder()
                .id(id)
                .status(status)
                .build();
        dishMapper.update(dish);
    }
}













