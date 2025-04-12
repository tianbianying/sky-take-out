package com.sky.service;

import com.sky.dto.*;
import com.sky.entity.Orders;
import com.sky.result.PageResult;
import com.sky.vo.OrderPaymentVO;
import com.sky.vo.OrderStatisticsVO;
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

    /**
     * @description: 接单
     * @title: updateStatus
     * @param: [id]
     */
    void updateStatus(Orders orders);

    /**
     * @description: 取消订单
     * @title: cancellationOfOrder
     * @param: [orders]
     */
    void cancellationOfOrder(OrdersCancelDTO ordersCancelDTO);

    /**
     * @description: 拒单
     * @title: decliningTheOrder
     * @param: [ordersRejectionDTO]
     */
    void decliningTheOrder(OrdersRejectionDTO ordersRejectionDTO);

    /**
     * @description: 查询各个状态的数量
     * @title: getStatusCount
     * @param: []
     */
    OrderStatisticsVO getStatusCount();

    /**
     * @description: 派送
     * @title: delivery
     * @param: [id]
     */
    void delivery(Long id);

    /**
     * @description: 完成订单
     * @title: complete
     * @param: [id]
     */
    void complete(Long id);

    /**
     * @description: 催单
     * @title: reminder
     * @param: [id]
     */
    void reminder(Long id);
}
