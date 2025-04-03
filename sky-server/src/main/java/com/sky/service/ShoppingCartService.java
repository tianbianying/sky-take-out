package com.sky.service;

import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.ShoppingCart;

import java.util.List;

/**
 * @description: 购物车相关接口的服务层接口
 * @author: Excell
 * @date: 2025年04月01日 15:22
 */
public interface ShoppingCartService {
    /**
     * @description: 为购物车表中添加数据
     * @title: add
     * @param: [shoppingCartDTO]
     */
    void add(ShoppingCartDTO shoppingCartDTO);

    /**
     * @return
     * @description: 获取购物车信息
     * @title: list
     * @param: []
     */
    List<ShoppingCart> list();

    /**
     * @description: 清空购物车
     * @title: cleanShoppingCart
     * @param: []
     */
    void cleanShoppingCart();

    /**
     * @description: 减少一件菜品
     * @title: delShoppingCartItem
     * @param: [shoppingCartDTO]
     */
    void delShoppingCartItem(ShoppingCartDTO shoppingCartDTO);
}
