package com.sky.mapper;

import com.sky.dto.OrdersPageQueryDTO;
import com.sky.entity.Orders;
import org.apache.ibatis.annotations.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @description:
 * @author: Excell
 * @date: 2025年04月07日 15:06
 */
@Mapper
public interface OrderMapper {
    /**
     * @description: 插入订单数据
     * @title: insert
     * @param: [orders]
     */
    @Insert("insert into orders(number, status, user_id, address_book_id, order_time, checkout_time, pay_method," +
            " pay_status, amount, remark, phone, address, user_name, consignee, cancel_reason, rejection_reason, " +
            "cancel_time, estimated_delivery_time, delivery_status, delivery_time, pack_amount, tableware_number, " +
            "tableware_status) " +
            "values " +
            "(#{number},#{status},#{userId},#{addressBookId},#{orderTime},#{checkoutTime},#{payMethod}," +
            "#{payStatus},#{amount},#{remark},#{phone},#{address},#{userName},#{consignee},#{cancelReason}," +
            "#{rejectionReason},#{cancelTime},#{estimatedDeliveryTime},#{deliveryStatus},#{deliveryTime}," +
            "#{packAmount},#{tablewareNumber},#{tablewareStatus})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insert(Orders orders);

    /**
     * 根据订单号查询订单
     *
     * @param orderNumber
     */
    @Select("select * from orders where number = #{orderNumber}")
    Orders getByNumber(String orderNumber);

    /**
     * 修改订单信息
     *
     * @param orders
     */
    void update(Orders orders);

    /**
     * 用于替换微信支付更新数据库状态的问题
     *
     * @param orderStatus
     * @param orderPaidStatus
     */
    @Update("update orders set status = #{orderStatus},pay_status = #{orderPaidStatus} ,checkout_time = #{check_out_time} " +
            "where number = #{orderNumber}")
    void updateStatus(Integer orderStatus, Integer orderPaidStatus, LocalDateTime check_out_time, String orderNumber);

    /**
     * @description: 根据用户id获取订单
     * @title: getOrderPage
     * @param: [ordersPageQueryDTO]
     */
    List<Orders> getOrderPage(OrdersPageQueryDTO ordersPageQueryDTO);

    /**
     * @description: 根据id查询订单
     * @title: getOrderById
     * @param: [id]
     */
    @Select("select * from orders where id=#{id}")
    Orders getOrderById(Long id);

    /**
     * @description: 根据状态查询订单数量
     * @title: getCount
     * @param: [status]
     */
    @Select("select count(*) from orders where status = #{status}")
    Integer getCount(Integer status);

    /**
     * @description: 通过时间和状态查找订单类
     * @title: getByOvertimeStatus
     * @param: [pendingPayment, localDateTime]
     */
    @Select("select * from orders where status = #{pendingPayment} and order_time < #{localDateTime}")
    List<Orders> getByOvertimeStatus(Integer pendingPayment, LocalDateTime localDateTime);
}
