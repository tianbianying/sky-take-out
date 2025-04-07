package com.sky.service;

import com.sky.dto.OrdersSubmitDTO;
import com.sky.vo.OrderSubmitVO;

/**
 * @description:
 * @author: Excell
 * @date: 2025年04月07日 15:04
 */
public interface OrderService {
    /**
     * @description: 提交订单
     * @title: submit
     * @param: [ordersSubmitDTO]
     */
    OrderSubmitVO submit(OrdersSubmitDTO ordersSubmitDTO);

}
