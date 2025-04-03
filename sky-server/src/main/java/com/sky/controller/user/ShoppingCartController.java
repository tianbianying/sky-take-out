package com.sky.controller.user;

import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.ShoppingCart;
import com.sky.result.Result;
import com.sky.service.ShoppingCartService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Delete;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @description:
 * @author: Excell
 * @data 2025年04月01日 15:15
 */
@RestController
@RequestMapping("/user/shoppingCart")
@Slf4j
@Api(tags = "C端-购物车相关接口")
public class ShoppingCartController {
    @Autowired
    ShoppingCartService shoppingCartService;

    /**
     * @description: 购物车添加接口
     * @title: add
     * @param: [shoppingCartDTO]
     */
    @PostMapping("/add")
    @ApiOperation("购物车添加")
    public Result add(@RequestBody ShoppingCartDTO shoppingCartDTO) {
        shoppingCartService.add(shoppingCartDTO);
        return Result.success();
    }

    /**
     * @description: 获取购物车信息
     * @title: list
     * @param: []
     */
    @GetMapping("/list")
    @ApiOperation("获取购物车信息")
    public Result<List<ShoppingCart>> list() {
        return Result.success(shoppingCartService.list());
    }

    /**
     * @description: 清空购物车
     * @title: delShoppingCart
     * @param: []
     */
    @DeleteMapping("/clean")
    @ApiOperation("清空购物车")
    public Result cleanShoppingCart() {
        shoppingCartService.cleanShoppingCart();
        return Result.success();
    }

    @PostMapping("/sub")
    @ApiOperation("删除一件商品")
    public Result delShoppingCartItem(@RequestBody ShoppingCartDTO shoppingCartDTO) {
        shoppingCartService.delShoppingCartItem(shoppingCartDTO);
        return Result.success();
    }
}
