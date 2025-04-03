package com.sky.service.impl;

import com.sky.constant.JwtClaimsConstant;
import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.Dish;
import com.sky.entity.Setmeal;
import com.sky.entity.ShoppingCart;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.mapper.ShoppingCartMapper;
import com.sky.service.ShoppingCartService;
import com.sky.utils.ThreadLocalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * @description: 购物车相关接口服务层的实现类
 * @author: Excell
 * @data 2025年04月01日 15:23
 */
@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {
    @Autowired
    ShoppingCartMapper shoppingCartMapper;
    @Autowired
    DishMapper dishMapper;
    @Autowired
    SetmealMapper setmealMapper;

    /**
     * @description: 为购物车添加数据
     * @title: add
     * @param: [shoppingCartDTO]
     */
    @Override
    public void add(ShoppingCartDTO shoppingCartDTO) {
        Long userId = Long.valueOf(((Map) ThreadLocalUtil.get()).get(JwtClaimsConstant.USER_ID).toString());
        ShoppingCart shoppingCart = ShoppingCart.builder()
                .userId(userId)
                .dishId(shoppingCartDTO.getDishId())
                .setmealId(shoppingCartDTO.getSetmealId())
                .dishFlavor(shoppingCartDTO.getDishFlavor())
                .build();

        // 查询判断是否已经存在
        List<ShoppingCart> shoppingCartList = shoppingCartMapper.getList(shoppingCart);
        if (shoppingCartList != null && !shoppingCartList.isEmpty()) {
            // 存在则在数量增加一
            ShoppingCart sc = shoppingCartList.get(0);
            sc.setNumber(sc.getNumber() + 1);
            // 重新进行插入
            shoppingCartMapper.updateNumber(sc);
            return;
        }

        // 不存在，存放到数据库中
        Dish dish = dishMapper.getDishById(shoppingCart.getDishId());

        if (dish != null) {
            shoppingCart.setName(dish.getName());
            shoppingCart.setAmount(dish.getPrice());
            shoppingCart.setImage(dish.getImage());
        } else {
            Setmeal setmeal = setmealMapper.getById(shoppingCart.getSetmealId());
            shoppingCart.setName(setmeal.getName());
            shoppingCart.setAmount(setmeal.getPrice());
            shoppingCart.setImage(setmeal.getImage());
        }
        shoppingCart.setNumber(1);
        shoppingCart.setCreateTime(LocalDateTime.now());
        shoppingCartMapper.insert(shoppingCart);
    }

    /**
     * @description: 获取购物车信息
     * @title: list
     * @param: []
     */
    @Override
    public List<ShoppingCart> list() {
        Long userId = Long.valueOf(((Map) ThreadLocalUtil.get()).get(JwtClaimsConstant.USER_ID).toString());
        ShoppingCart shoppingCart = ShoppingCart.builder()
                .userId(userId)
                .build();
        List<ShoppingCart> shoppingCarts = shoppingCartMapper.getList(shoppingCart);
        return shoppingCarts;
    }

    /**
     * @description: 清空购物车
     * @title: cleanShoppingCart
     * @param: []
     */
    @Override
    public void cleanShoppingCart() {
        Long userId = Long.valueOf(((Map) ThreadLocalUtil.get()).get(JwtClaimsConstant.USER_ID).toString());
        shoppingCartMapper.clean(userId);
    }

    /**
     * @description: 减少一件商品
     * @title: delShoppingCartItem
     * @param: [shoppingCartDTO]
     */
    @Override
    public void delShoppingCartItem(ShoppingCartDTO shoppingCartDTO) {
        Long userId = Long.valueOf(((Map) ThreadLocalUtil.get()).get(JwtClaimsConstant.USER_ID).toString());
        ShoppingCart shoppingCart = ShoppingCart.builder()
                .userId(userId)
                .dishId(shoppingCartDTO.getDishId())
                .setmealId(shoppingCartDTO.getSetmealId())
                .dishFlavor(shoppingCartDTO.getDishFlavor())
                .build();
        // 根据条件获取商品
        List<ShoppingCart> shoppingCarts = shoppingCartMapper.getList(shoppingCart);
        ShoppingCart sc = shoppingCarts.get(0);
        // 判断商品的数量是否大于一
        if (sc.getNumber() > 1) {
            // 大于，减少一件商品
            sc.setNumber(sc.getNumber() - 1);
            shoppingCartMapper.updateNumber(sc);
        } else {
            // 不大于，删除表中数据
            shoppingCartMapper.deleteById(sc.getId());
        }
    }
}
