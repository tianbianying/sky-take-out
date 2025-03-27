package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.annotation.AutoAnnotation;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.enumeration.OperationType;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface SetmealMapper {

    /**
     * 根据分类id查询套餐的数量
     *
     * @param id
     * @return
     */
    @Select("select count(id) from setmeal where category_id = #{categoryId}")
    Integer countByCategoryId(Long id);

    @AutoAnnotation(OperationType.INSERT)
    @Insert("insert into setmeal (category_id, name, price, description, image, create_time, update_time, create_user, update_user) " +
            "values (#{categoryId}, #{name}, #{price}, #{description}, #{image}, #{createTime}, #{updateTime}, #{createUser}, #{updateUser})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insert(Setmeal setmeal);

    /**
     * @description: 根据条件获取套餐列表
     * @title: getList
     * @param: [setmealPageQueryDTO]
     */
    Page<Setmeal> getList(SetmealPageQueryDTO setmealPageQueryDTO);

    /**
     * @description: 根据id删除套餐表
     * @title: delSetmeal
     * @param: [id]
     */
    void delSetmeal(List<Long> ids);

    /**
     * @description: 根据id修改数据
     * @title: updateById
     * @param: [setmeal]
     */
    @AutoAnnotation(OperationType.UPDATE)
    void updateById(Setmeal setmeal);

    /**
     * @description: 通过id获取套餐信息
     * @title: getById
     * @param: [id]
     */
    @Select("select id," +
            "       category_id," +
            "       name," +
            "       price," +
            "       status," +
            "       description," +
            "       image," +
            "       create_time," +
            "       update_time," +
            "       create_user," +
            "       update_user " +
            "from setmeal " +
            "where id=#{id}")
    Setmeal getById(Long id);
}
