package com.sky.controller.user;

import com.sky.dto.OrdersSubmitDTO;
import com.sky.result.Result;
import com.sky.service.OrderService;
import com.sky.vo.OrderSubmitVO;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @description: 订单相关接口
 * @author: Excell
 * @data 2025年04月07日 14:55
 */
@RestController("userOrderController")
@RequestMapping("/user/order")
@Api(tags = "提交订单相关接口")
@Slf4j
public class OrdersController {
    @Autowired
    private OrderService orderService;

    /**
     * @description: 提交订单数据
     * @title: submitOrder
     * @param: [ordersSubmitDTO]
     */
    @PostMapping("/submit")
    public Result<OrderSubmitVO> submitOrder(@RequestBody OrdersSubmitDTO ordersSubmitDTO) {
        return Result.success(orderService.submit(ordersSubmitDTO));
    }

}
