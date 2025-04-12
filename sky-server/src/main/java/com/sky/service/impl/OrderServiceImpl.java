package com.sky.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.JwtClaimsConstant;
import com.sky.constant.MessageConstant;
import com.sky.dto.*;
import com.sky.entity.*;
import com.sky.exception.AddressBookBusinessException;
import com.sky.exception.OrderBusinessException;
import com.sky.exception.ShoppingCartBusinessException;
import com.sky.mapper.*;
import com.sky.result.PageResult;
import com.sky.service.OrderService;
import com.sky.utils.ThreadLocalUtil;
import com.sky.vo.OrderPaymentVO;
import com.sky.vo.OrderStatisticsVO;
import com.sky.vo.OrderSubmitVO;
import com.sky.vo.OrderVO;
import com.sky.websocket.WebSocketServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
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
    @Autowired
    private WebSocketServer webSocketServer;

    /**
     * @description: 下单，返回订单编号
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
     * 订单支付，这个函数调用完毕返回后，用户界面就显示出支付界面
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
        Orders orders = orderMapper.getByNumber(orderNumber);
        Map map = new HashMap<>();
        map.put("type", 1);
        map.put("orderId", orders.getId());
        map.put("content", "订单号：" + orderNumber);
        String json = JSON.toJSONString(map);

        webSocketServer.sendToAllClient(json);
        return vo;
    }

    /**
     * 支付成功，修改订单状态，这个方法因为修改所以没有用到
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
        try {
            // 从 ThreadLocal 中获取数据并转换为 Long 类型的用户ID
            Long userId = Long.valueOf(((Map) ThreadLocalUtil.get()).get(JwtClaimsConstant.USER_ID).toString());

            // 如果获取到的 userId 是有效的，则设置到 ordersPageQueryDTO
            ordersPageQueryDTO.setUserId(userId);
        } catch (NumberFormatException | NullPointerException e) {
            // 捕获 NumberFormatException 或 NullPointerException，表示无法从 ThreadLocal 获取有效的用户ID
            // 如果需要，可以记录日志或抛出自定义异常
            // 如果不设置用户ID，则不做任何操作
            // 可选：可以在这里抛出一个 RuntimeException 或记录日志
            // logger.error("Failed to retrieve valid userId from ThreadLocal", e);
        } finally {
            // 可选的 finally 块，执行清理工作等
        }

        // 2.根据id获取用户历史订单
        List<Orders> orders = orderMapper.getOrderPage(ordersPageQueryDTO);
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

    /**
     * @description: 接单
     * @title: updateStatus
     * @param: [id]
     */
    @Override
    public void updateStatus(Orders orders) {
        orders.setStatus(Orders.CONFIRMED);
        orderMapper.update(orders);
    }

    /**
     * @description: 订单取消
     * @title: cancellationOfOrder
     * @param: [ordersCancelDTO]
     */
    @Transactional
    @Override
    public void cancellationOfOrder(OrdersCancelDTO ordersCancelDTO) {
        processOrderRefundAndUpdate(
                ordersCancelDTO.getId(),
                Orders.CANCELLED,
                orders -> {
                    orders.setCancelReason(ordersCancelDTO.getCancelReason());
                }
        );
    }

    /**
     * @description: 拒单
     * @title: decliningTheOrder
     * @param: [ordersRejectionDTO]
     */
    @Transactional
    @Override
    public void decliningTheOrder(OrdersRejectionDTO ordersRejectionDTO) {
        processOrderRefundAndUpdate(
                ordersRejectionDTO.getId(),
                Orders.CANCELLED,
                orders -> {
                    orders.setRejectionReason(ordersRejectionDTO.getRejectionReason());
                }
        );
    }

    /**
     * @description: 查询各个状态订单的数量
     * @title: getStatusCount
     * @param: []
     */
    @Override
    public OrderStatisticsVO getStatusCount() {
        Integer toBeConfirmedCount = orderMapper.getCount(Orders.TO_BE_CONFIRMED);
        Integer confirmedCount = orderMapper.getCount(Orders.CONFIRMED);
        Integer deliveryInProgressCount = orderMapper.getCount(Orders.DELIVERY_IN_PROGRESS);

        return OrderStatisticsVO
                .builder()
                .toBeConfirmed(toBeConfirmedCount)
                .confirmed(confirmedCount)
                .deliveryInProgress(deliveryInProgressCount)
                .build();
    }

    /**
     * @description: 派送订单
     * @title: delivery
     * @param: [id]
     */
    @Override
    public void delivery(Long id) {
        Orders orders = Orders.
                builder()
                .id(id)
                .status(Orders.DELIVERY_IN_PROGRESS)
                .build();
        orderMapper.update(orders);
    }

    /**
     * @description: 完成订单
     * @title: complete
     * @param: [id]
     */
    @Override
    public void complete(Long id) {
        Orders orders = Orders.
                builder()
                .id(id)
                .status(Orders.COMPLETED)
                .build();
        orderMapper.update(orders);
    }

    /**
     * @description: 催单
     * @title: reminder
     * @param: [id]
     */
    @Override
    public void reminder(Long id) {
        Orders orders = orderMapper.getOrderById(id);
        if (orders == null) {
            throw new OrderBusinessException(MessageConstant.ORDER_STATUS_ERROR);
        }

        // 获取订单号码
        Map map = new HashMap<>();
        map.put("type", 1);
        map.put("orderId", orders.getId());
        map.put("content", "订单号：" + orders.getNumber());
        String json = JSON.toJSONString(map);

        webSocketServer.sendToAllClient(json);
    }

    /**
     * 通用退款处理 + 状态更新逻辑
     */
    private void processOrderRefundAndUpdate(Long orderId, Integer newStatus, Consumer<Orders> reasonSetter) {
        Orders ordersDB = orderMapper.getOrderById(orderId);
        if (ordersDB == null) {
            throw new RuntimeException("订单不存在，ID: " + orderId);
        }

        if (ordersDB.getPayStatus() != null && ordersDB.getPayStatus() == 1) {
            // 模拟退款
            log.info("给订单{}退款", ordersDB.getNumber());

            // 真退款时调用微信接口
            // String refund = weChatPayUtil.refund(...);
            // log.info("申请退款：{}", refund);
        }

        Orders updateOrder = new Orders();
        updateOrder.setId(orderId);
        updateOrder.setPayStatus(Orders.REFUND);
        updateOrder.setStatus(newStatus);
        updateOrder.setCancelTime(LocalDateTime.now());

        // 设置取消/拒绝原因
        reasonSetter.accept(updateOrder);

        orderMapper.update(updateOrder);
    }


}
