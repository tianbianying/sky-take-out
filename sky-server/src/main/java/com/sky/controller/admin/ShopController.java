package com.sky.controller.admin;

import com.sky.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @description: 店铺相关接口
 * @author: Excell
 * @data 2025年03月25日 19:26
 */
@RestController("adminShopControllerBean")
@RequestMapping("/admin/shop")
@Api(tags = "店铺营业状态相关接口")
@Slf4j
public class ShopController {
    @Autowired
    RedisTemplate redisTemplate;

    private final static String key = "SHOP_STATUS";

    /**
     * @description: 更改店铺状态
     * @title: setStatus
     * @param: [status]
     */
    @PutMapping("/{status}")
    @ApiOperation("更改店铺状态")
    public Result setStatus(@PathVariable Integer status) {
        redisTemplate.opsForValue().set(key, status);
        return Result.success();
    }

    /**
     * @description: 获取店铺状态
     * @title: getStatus
     * @param: []
     */
    @GetMapping("/status")
    @ApiOperation("获取店铺状态")
    public Result<Integer> getStatus() {
        return Result.success((Integer) redisTemplate.opsForValue().get(key));
    }
}
