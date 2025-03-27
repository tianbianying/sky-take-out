package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.annotation.AutoAnnotation;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import com.sky.enumeration.OperationType;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

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

    /**
     * @description: 添加新的菜品分类
     * @title: addCategory
     * @param: [category]
     */
    @AutoAnnotation(value = OperationType.INSERT)
    @Insert("insert into category (id, type, name, sort, status, create_time, update_time, create_user, update_user) values " +
            "(#{id}, #{type}, #{name}, #{sort}, #{status}, #{createTime}, #{updateTime}, #{createUser}, #{updateUser})")
    void addCategory(Category category);

    /**
     * @description: 根据id删除分类
     * @title: delById
     * @param: [id]
     */
    @Delete("delete from category where id=#{id}")
    void delById(Long id);

    /**
     * @description: 通过id进行分类数据的修改
     * @title: update
     * @param: [category]
     */
    @AutoAnnotation(value = OperationType.UPDATE)
    void update(Category category);

    /**
     * @description: 根据类型查询分类
     * @title: getListByType
     * @param: [type]
     */
    @Select("select id, type, name, sort, status, create_time, update_time, create_user, update_user from " +
            "category where type=#{type}")
    List<Category> getListByType(Integer type);
}
















