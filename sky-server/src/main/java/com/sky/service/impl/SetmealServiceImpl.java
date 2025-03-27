package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.entity.SetmealDish;
import com.sky.mapper.SetmealDishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.result.PageResult;
import com.sky.service.SetmealService;
import com.sky.vo.SetmealVO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @description: 套餐类的服务层
 * @author: Excell
 * @data 2025年03月21日 15:47
 */
@Service
public class SetmealServiceImpl implements SetmealService {
    @Resource
    private SetmealMapper setmealMapper;
    @Resource
    private SetmealDishMapper setmealDishMapper;

    /**
     * @description: 增加新的套餐
     * @title: saveSetmeal
     * @param: [setmealVO]
     */
    @Override
    @Transactional
    public void saveSetmeal(SetmealVO setmealVO) {
        // 1.获取套餐和菜品关系
        List<SetmealDish> setmealDishes = setmealVO.getSetmealDishes();

        // 2.将新的套餐添加到套餐表中
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealVO, setmeal);
        setmealMapper.insert(setmeal);

        // 3.将套餐和菜品关系添加到套餐菜品关系表
        if (setmealDishes != null && !setmealDishes.isEmpty()) {
            setmealDishes.forEach(setmealDish -> setmealDish.setSetmealId(setmeal.getId()));
            setmealDishMapper.insert(setmealDishes);
        }


    }

    /**
     * @description: 获取套餐分页列表
     * @title: pageList
     * @param: [setmealPageQueryDTO]
     */
    @Override
    public PageResult pageList(SetmealPageQueryDTO setmealPageQueryDTO) {
        // 开启分页插件
        PageHelper.startPage(setmealPageQueryDTO.getPage(), setmealPageQueryDTO.getPageSize());

        Page<Setmeal> setmeals = setmealMapper.getList(setmealPageQueryDTO);

        return new PageResult(setmeals.getTotal(), setmeals.getResult());
    }

    /**
     * @description: 删除套餐
     * @title: delSetmeal
     * @param: [id]
     */
    @Override
    @Transactional
    public void delSetmeal(List<Long> ids) {
        // 根据id进行套餐菜品表数据的删除
        setmealDishMapper.delSetmealDish(ids);
        // 根据id进行套餐表的数据的删除
        setmealMapper.delSetmeal(ids);

    }

    /**
     * @description: 根据id进行套餐的修改
     * @title: updateSetmeal
     * @param: [id]
     */
    @Override
    @Transactional
    public void updateSetmeal(SetmealVO setmealVO) {
        // 1.获取套餐菜品关系
        List<SetmealDish> setmealDishes = setmealVO.getSetmealDishes();

        // 2.获取套餐id,设置套餐菜品表id属性
        Long id = setmealVO.getId();
        setmealDishes.forEach(setmealDish -> setmealDish.setSetmealId(id));

        // 3.根据套餐id删除旧的套餐菜品数据，插入新的套餐菜品数据
        ArrayList<Long> arrId = new ArrayList<>();
        arrId.add(id);
        setmealDishMapper.delSetmealDish(arrId);
        setmealDishMapper.insert(setmealDishes);
        // 4.修改套餐数据
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealVO, setmeal);
        setmealMapper.updateById(setmeal);

    }

    /**
     * @description: 通过id获取套餐信息
     * @title: getSetmealById
     * @param: [id]
     */
    @Override
    public Setmeal getSetmealById(Long id) {
        return setmealMapper.getById(id);
    }

    /**
     * @description: 修改起售停售状态
     * @title: startOrStop
     * @param: [status]
     */
    @Override
    public void startOrStop(Integer status, Long id) {
        Setmeal setmeal = Setmeal.builder()
                .id(id)
                .status(status)
                .build();
        setmealMapper.updateById(setmeal);
    }
}







