package com.sky.mapper;

import com.sky.entity.OrderDetail;
import org.apache.ibatis.annotations.Mapper;

import java.util.ArrayList;

/**
 * @description: 用户订单详情
 * @author: Excell
 * @date: 2025年04月07日 18:59
 */
@Mapper
public interface OrderDetailMapper {
    void insertList(ArrayList<OrderDetail> orderDetails);
}
