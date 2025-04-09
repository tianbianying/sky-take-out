package com.sky.mapper;

import com.sky.entity.OrderDetail;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.ArrayList;
import java.util.List;

/**
 * @description: 用户订单详情
 * @author: Excell
 * @date: 2025年04月07日 18:59
 */
@Mapper
public interface OrderDetailMapper {
    /**
     * @description: 批量插入用户详情数据
     * @title: insertList
     * @param: [orderDetails]
     */
    void insertList(ArrayList<OrderDetail> orderDetails);

    /**
     * @description: 根据订单id查询订单详情
     * @title: getOrderDetailByOrderId
     * @param: [OrderId]
     */
    @Select("select id," +
            "       name," +
            "       image," +
            "       order_id," +
            "       dish_id," +
            "       setmeal_id," +
            "       dish_flavor," +
            "       number," +
            "       amount " +
            "from order_detail " +
            "where order_id =#{OrderId}")
    List<OrderDetail> getOrderDetailByOrderId(Long OrderId);
}
