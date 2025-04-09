package com.sky.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.JwtClaimsConstant;
import com.sky.constant.MessageConstant;
import com.sky.context.BaseContext;
import com.sky.dto.OrdersPageQueryDTO;
import com.sky.dto.OrdersPaymentDTO;
import com.sky.dto.OrdersSubmitDTO;
import com.sky.entity.*;
import com.sky.exception.AddressBookBusinessException;
import com.sky.exception.OrderBusinessException;
import com.sky.exception.ShoppingCartBusinessException;
import com.sky.mapper.*;
import com.sky.result.PageResult;
import com.sky.service.OrderService;
import com.sky.utils.ThreadLocalUtil;
import com.sky.utils.WeChatPayUtil;
import com.sky.vo.OrderPaymentVO;
import com.sky.vo.OrderSubmitVO;
import com.sky.vo.OrderVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @description:
 * @author: Excell
 * @data 2025年04月07日 15:05
 */
@Service
@Slf4j
public class OrderServiceImpl implements OrderService {
    @Autowired
    OrderMapper orderMapper;
    @Autowired
    AddressBookMapper addressBookMapper;
    @Autowired
    ShoppingCartMapper shoppingCartMapper;
    @Autowired
    OrderDetailMapper orderDetailMapper;
    @Autowired
    private UserMapper userMapper;

    /**
     * @description: 提交订单
     * @title: submit
     * @param: [ordersSubmitDTO]
     */
    @Transactional
    @Override
    public OrderSubmitVO submit(OrdersSubmitDTO ordersSubmitDTO) {
        // 1.判断地址是否存在
        AddressBook addressBook = addressBookMapper.getById(ordersSubmitDTO.getAddressBookId());
        if (addressBook == null) {
            throw new AddressBookBusinessException(MessageConstant.ADDRESS_BOOK_IS_NULL);
        }
        // 2.判断购物车是否为空
        Long userId = Long.valueOf(((Map) ThreadLocalUtil.get()).get(JwtClaimsConstant.USER_ID).toString());
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setUserId(userId);
        List<ShoppingCart> shoppingCarts = shoppingCartMapper.getList(shoppingCart);
        if (shoppingCarts == null || shoppingCarts.isEmpty()) {
            throw new ShoppingCartBusinessException(MessageConstant.SHOPPING_CART_IS_NULL);
        }
        // 3.将订单数据插入订单表中
        Orders orders = new Orders();
        BeanUtils.copyProperties(ordersSubmitDTO, orders);
        orders.setPayStatus(Orders.UN_PAID);
        orders.setOrderTime(LocalDateTime.now());
        orders.setStatus(Orders.PENDING_PAYMENT);
        orders.setPhone(addressBook.getPhone());
        orders.setConsignee(addressBook.getConsignee());
        orders.setUserId(userId);
        orders.setAddress(addressBook.getDetail());
        orders.setNumber(String.valueOf(System.currentTimeMillis()));
        orderMapper.insert(orders);

        // 4.将订单详情数据插入订单详情表中
        ArrayList<OrderDetail> orderDetails = new ArrayList<>();
        shoppingCarts.forEach(shoppingCartItem -> {
            OrderDetail orderDetail = new OrderDetail();
            BeanUtils.copyProperties(shoppingCartItem, orderDetail);
            orderDetail.setOrderId(orders.getId());
            orderDetails.add(orderDetail);
        });
        orderDetailMapper.insertList(orderDetails);

        // 5.清空购物车
        shoppingCartMapper.clean(userId);
        // 6.将返回的数据封装返回
        OrderSubmitVO orderSubmitVO = OrderSubmitVO.builder()
                .id(orders.getId())
                .orderNumber(orders.getNumber())
                .orderAmount(orders.getAmount())
                .orderTime(orders.getOrderTime())
                .build();
        return orderSubmitVO;
    }

    /**
     * 订单支付
     *
     * @param ordersPaymentDTO
     * @return
     */
    public OrderPaymentVO payment(OrdersPaymentDTO ordersPaymentDTO) throws Exception {
        // 当前登录用户id
        Long userId = Long.valueOf(((Map) ThreadLocalUtil.get()).get(JwtClaimsConstant.USER_ID).toString());
        User user = userMapper.getById(userId);

        // 调用微信支付接口，生成预支付交易单
        /*JSONObject jsonObject = weChatPayUtil.pay(
                ordersPaymentDTO.getOrderNumber(), //商户订单号
                new BigDecimal(0.01), //支付金额，单位 元
                "苍穹外卖订单", //商品描述
                user.getOpenid() //微信用户的openid
        );

        if (jsonObject.getString("code") != null && jsonObject.getString("code").equals("ORDERPAID")) {
            throw new OrderBusinessException("该订单已支付");
        }*/
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", "ORDERPAID");
        OrderPaymentVO vo = jsonObject.toJavaObject(OrderPaymentVO.class);
        vo.setPackageStr(jsonObject.getString("package"));

        // 为替代微信支付成功后的数据库订单状态更新，多定义一个方法进行修改
        Integer OrderPaidStatus = Orders.PAID; // 支付状态，已支付
        Integer OrderStatus = Orders.TO_BE_CONFIRMED;  // 订单状态，待接单

        // 发现没有将支付时间 check_out属性赋值，所以在这里更新
        LocalDateTime check_out_time = LocalDateTime.now();

        // 获取订单号码
        String orderNumber = ordersPaymentDTO.getOrderNumber();

        log.info("调用updateStatus，用于替换微信支付更新数据库状态的问题");
        orderMapper.updateStatus(OrderStatus, OrderPaidStatus, check_out_time, orderNumber);
        return vo;
    }

    /**
     * 支付成功，修改订单状态
     *
     * @param outTradeNo
     */
    public void paySuccess(String outTradeNo) {

        // 根据订单号查询订单
        Orders ordersDB = orderMapper.getByNumber(outTradeNo);

        // 根据订单id更新订单的状态、支付方式、支付状态、结账时间
        Orders orders = Orders.builder()
                .id(ordersDB.getId())
                .status(Orders.TO_BE_CONFIRMED)
                .payStatus(Orders.PAID)
                .checkoutTime(LocalDateTime.now())
                .build();

        orderMapper.update(orders);
    }

    /**
     * @description: 根据订单id获取订单详情
     * @title: getOrder
     * @param: [id]
     */
    @Override
    @Transactional
    public OrderVO getOrder(String id) {
        // 1.根据订单id查询订单菜品信息，并返回订单对象
        Orders orders = orderMapper.getOrderById(Long.valueOf(id));

        // 2.根据订单id查询订单详情，并放到List集合
        List<OrderDetail> orderDetails = orderDetailMapper.getOrderDetailByOrderId(Long.valueOf(id));

        // 3.封装为OrderVo对象
        OrderVO orderVO = new OrderVO();
        BeanUtils.copyProperties(orders, orderVO);
        orderVO.setOrderDetailList(orderDetails);
        return orderVO;
    }

    /**
     * @description: 分页查询历史订单
     * @title: getOrderHistory
     * @param: [ordersPageQueryDTO]
     */
    @Override
    @Transactional
    public PageResult getOrderHistory(OrdersPageQueryDTO ordersPageQueryDTO) {
        PageHelper.startPage(ordersPageQueryDTO.getPage(), ordersPageQueryDTO.getPageSize());
        // 1.获取用户id
        Long userId = Long.valueOf(((Map) ThreadLocalUtil.get()).get(JwtClaimsConstant.USER_ID).toString());
        ordersPageQueryDTO.setUserId(userId);
        // 2.根据id获取用户历史订单
        List<Orders> orders = orderMapper.getOrderByUserId(ordersPageQueryDTO);
        Page<OrderVO> orderVOS = new Page<>();
        // 3.根据订单id查询订单详情
        orders.forEach(order -> {
            OrderVO orderVO = new OrderVO();
            BeanUtils.copyProperties(order, orderVO);
            // 根据订单id查询订单详情
            List<OrderDetail> orderDetails = orderDetailMapper.getOrderDetailByOrderId(orderVO.getId());
            orderVO.setOrderDetailList(orderDetails);
            orderVOS.add(orderVO);
        });
        // 4.返回数据
        return new PageResult(orderVOS.getTotal(), orderVOS.getResult());
    }

    /**
     * @description: 再来一单
     * @title: oneMoreOrder
     * @param: [id]
     */
    @Override
    @Transactional
    public void oneMoreOrder(Long id) {
        // 1.根据id获取订单详情
        ArrayList<OrderDetail> orderDetails = (ArrayList<OrderDetail>) orderDetailMapper.getOrderDetailByOrderId(id);
        List<ShoppingCart> shoppingCarts = orderDetails.stream().map(orderDetail -> {
            ShoppingCart shoppingCart = new ShoppingCart();
            BeanUtils.copyProperties(orderDetail, shoppingCart);
            shoppingCart.setUserId(Long.valueOf(((Map) ThreadLocalUtil.get()).get(JwtClaimsConstant.USER_ID).toString()));
            shoppingCart.setCreateTime(LocalDateTime.now());
            return shoppingCart;
        }).collect(Collectors.toList());

        shoppingCarts.forEach(shoppingCart -> {
            shoppingCartMapper.insert(shoppingCart);
        });
    }

}
