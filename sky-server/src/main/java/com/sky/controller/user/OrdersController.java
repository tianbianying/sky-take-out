package com.sky.controller.user;

import com.sky.dto.OrdersPaymentDTO;
import com.sky.dto.OrdersSubmitDTO;
import com.sky.result.Result;
import com.sky.service.OrderService;
import com.sky.vo.OrderPaymentVO;
import com.sky.vo.OrderSubmitVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    @ApiOperation("提交订单数据")
    public Result<OrderSubmitVO> submitOrder(@RequestBody OrdersSubmitDTO ordersSubmitDTO) {
        return Result.success(orderService.submit(ordersSubmitDTO));
    }

    /**
     * 订单支付
     *
     * @param ordersPaymentDTO
     * @return
     */
    @PutMapping("/payment")
    @ApiOperation("订单支付")
    public Result<OrderPaymentVO> payment(@RequestBody OrdersPaymentDTO ordersPaymentDTO) throws Exception {
        log.info("订单支付：{}", ordersPaymentDTO);
        OrderPaymentVO orderPaymentVO = orderService.payment(ordersPaymentDTO);
        log.info("生成预支付交易单：{}", orderPaymentVO);
        return Result.success(orderPaymentVO);
    }

}
