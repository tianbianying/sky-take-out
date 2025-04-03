package com.sky.mapper;

import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.ShoppingCart;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * @description: 购物车相关接口的持久层
 * @author: Excell
 * @date: 2025年04月01日 15:24
 */
@Mapper
public interface ShoppingCartMapper {
    /**
     * @description: 根据条件获取购物车信息
     * @title: getList
     * @param: [shoppingCart]
     */
    List<ShoppingCart> getList(ShoppingCart shoppingCart);

    /**
     * @description: 修改商品数量
     * @title: insert
     * @param: [sc]
     */
    @Update("update shopping_cart set number=#{number} where id=#{id}")
    void updateNumber(ShoppingCart sc);

    /**
     * @description: 添加购物车中的数据
     * @title: insert
     * @param: [shoppingCart]
     */
    @Insert("insert into shopping_cart (name, image, user_id, dish_id, setmeal_id, dish_flavor, amount, create_time) values " +
            "(#{name},#{image},#{userId},#{dishId},#{setmealId},#{dishFlavor},#{amount},#{createTime})")
    void insert(ShoppingCart shoppingCart);

    /**
     * @description: 根据用户id清空购物车
     * @title: clean
     * @param: [userId]
     */
    @Delete("delete from shopping_cart where user_id=#{userId}")
    void clean(Long userId);


    /**
     * @description: 根据id删除用户
     * @title: deleteById
     * @param: [id]
     */
    @Delete("delete from shopping_cart where id=#{id}")
    void deleteById(Long id);
}
