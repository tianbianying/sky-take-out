package com.sky.mapper;

import com.sky.entity.Orders;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;

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
}
