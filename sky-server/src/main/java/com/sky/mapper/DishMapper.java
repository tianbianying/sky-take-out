package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.annotation.AutoAnnotation;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.enumeration.OperationType;
import com.sky.vo.DishVO;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface DishMapper {

    /**
     * 根据分类id查询菜品数量
     *
     * @param categoryId
     * @return
     */
    @Select("select count(id) from dish where category_id = #{categoryId}")
    Integer countByCategoryId(Long categoryId);

    /**
     * @description: 新增菜品
     * @title: insert
     * @param: [dish]
     */
    @AutoAnnotation(OperationType.INSERT)
    @Insert("insert into dish (name,category_id,price,image,description,status,create_time,update_time,create_user,update_user) " +
            "values " +
            "(#{name},#{categoryId},#{price},#{image},#{description},#{status},#{createTime},#{updateTime},#{createUser},#{updateUser})")
    // 允许使用数据库中的主键，将主键的值赋给dish对象中的id属性
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insert(Dish dish);

    /**
     * @description: 根据条件查询数据
     * @title: list
     * @param: [dishPageQueryDTO]
     */
    Page<DishVO> list(DishPageQueryDTO dishPageQueryDTO);

    /**
     * @description: 根据id获取菜品状态
     * @title: getStatus
     * @param: [ids]
     */
    List<Integer> getStatus(List<Long> ids);

    /**
     * @description: 根据菜品id批量删除菜品
     * @title: delByIds
     * @param: [dishIds]
     */
    void delByIds(List<Long> dishIds);

    /**
     * @description: 根据id获取Dish对象
     * @title: getDishById
     * @param: [id]
     */
    @Select("select d.id," +
            " d.name," +
            " d.category_id," +
            " d.price," +
            " d.image," +
            " d.description," +
            " d.status," +
            " d.create_time," +
            " d.update_time," +
            " d.create_user," +
            " d.update_user," +
            " c.name as category_name " +
            "from dish d" +
            "         left join category c on d.category_id = c.id " +
            "where d.id = #{id} ")
    Dish getDishById(Long id);

    /**
     * @description: 根据分类id查询菜品
     * @title: getDishByCategoryId
     * @param: [categoryId]
     */
    @Select("select id, name, category_id, price, image, description, status, create_time, update_time, create_user, update_user " +
            "from dish " +
            "where category_id = #{categoryId}")
    List<Dish> getDishByCategoryId(Long categoryId);

    /**
     * @description: 修改菜品表
     * @title: update
     * @param: [dish]
     */
    @AutoAnnotation(OperationType.UPDATE)
    void update(Dish dish);


}













