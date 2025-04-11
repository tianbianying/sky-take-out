package com.sky.task;

import com.sky.constant.MessageConstant;
import com.sky.entity.Orders;
import com.sky.mapper.OrderMapper;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @description: 订单定时类
 * @author: Excell
 * @data 2025年04月11日 15:48
 */
@Component
@Slf4j
public class OrderTask {
    @Autowired
    OrderMapper orderMapper;

    /**
     * @description: 支付超时解决方法
     * @title: overtimePayment
     * @param: []
     */
    @Scheduled(cron = "0 * * * * *")
    public void overtimePayment() {
        LocalDateTime localDateTime = LocalDateTime.now().minusMinutes(15L);
        // 根据条件查询
        List<Orders> orders = orderMapper.getByOvertimeStatus(Orders.PENDING_PAYMENT, localDateTime);

        // 将查询到的订单状态改为取消状态
        orders.forEach(order -> {
            order.setStatus(Orders.CANCELLED);
            order.setCancelTime(LocalDateTime.now());
            order.setCancelReason(MessageConstant.ORDER_TIMEOUT);
            // 修改
            orderMapper.update(order);
        });
    }

    /**
     * @description: 订单派送未完成解决方法
     * @title:
     * @param: []
     */
    @Scheduled(cron = "0 0 1 * * *")
    public void deliveryTimeout() {
        LocalDateTime localDateTime = LocalDateTime.now().minusHours(1L);
        // 根据条件查询
        List<Orders> orders = orderMapper.getByOvertimeStatus(Orders.DELIVERY_IN_PROGRESS, localDateTime);

        // 将查询到的订单状态改为完成状态
        orders.forEach(order -> {
            order.setStatus(Orders.COMPLETED);
            order.setCancelTime(LocalDateTime.now());
            // 修改
            orderMapper.update(order);
        });
    }


}
