package com.sky.controller.user;

import com.sky.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @description: 用户获取店铺状态
 * @author: Excell
 * @data 2025年03月25日 19:40
 */
@RestController("userShopControllerBean")
@RequestMapping("/user/shop")
@Api(tags = "店铺营业状态相关接口")
@Slf4j
public class ShopController {
    @Autowired
    RedisTemplate redisTemplate;

    private final static String key = "SHOP_STATUS";

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
