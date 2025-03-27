package com.sky.mapper;

import com.sky.entity.DishFlavor;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @description: 口味表的Mapper层
 * @author: Excell
 * @date: @data 2025年03月20日 09:26
 */
@Mapper
public interface FlavorMapper {
    /**
     * @description: 新增口味
     * @title: insert
     * @param: [flavors]
     */
    void insert(List<DishFlavor> flavors);

    /**
     * @description: 根据传输过来的菜品id进行数据的删除
     * @title: delByIds
     * @param: [dishIds]
     */
    void delByIds(List<Long> dishIds);

    /**
     * @description: 根据传输过来的菜品id获取口味
     * @title: getById
     * @param: [dishId]
     */
    @Select("select id, dish_id, name, value" +
            " from dish_flavor" +
            " where dish_id = #{dishId}")
    List<DishFlavor> getById(Long dishId);
}
