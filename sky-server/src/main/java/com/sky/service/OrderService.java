package com.sky.service;

import com.sky.dto.OrdersPageQueryDTO;
import com.sky.dto.OrdersPaymentDTO;
import com.sky.dto.OrdersSubmitDTO;
import com.sky.result.PageResult;
import com.sky.vo.OrderPaymentVO;
import com.sky.vo.OrderSubmitVO;
import com.sky.vo.OrderVO;

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

    /**
     * 订单支付
     * @param ordersPaymentDTO
     * @return
     */
    OrderPaymentVO payment(OrdersPaymentDTO ordersPaymentDTO) throws Exception;

    /**
     * 支付成功，修改订单状态
     * @param outTradeNo
     */
    void paySuccess(String outTradeNo);

    /**
     * @description: 根据订单id获取订单详情
     * @title: getOrder
     * @param: [id]
     */
    OrderVO getOrder(String id);

    /**
     * @description: 获取历史订单
     * @title: getOrderHistory
     * @param: [ordersPageQueryDTO]
     */
    PageResult getOrderHistory(OrdersPageQueryDTO ordersPageQueryDTO);

    /**
     * @description: 根据id查询插入
     * @title: oneMoreOrder
     * @param: [id]
     */
    void oneMoreOrder(Long id);
}
