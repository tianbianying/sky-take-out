package com.sky.controller.admin;

import com.sky.dto.OrdersCancelDTO;
import com.sky.dto.OrdersPageQueryDTO;
import com.sky.dto.OrdersRejectionDTO;
import com.sky.entity.Orders;
import com.sky.mapper.OrderMapper;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.OrderService;
import com.sky.vo.OrderOverViewVO;
import com.sky.vo.OrderStatisticsVO;
import com.sky.vo.OrderVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @description:
 * @author: Excell
 * @data 2025年04月09日 21:20
 */
@RestController("adminOrderController")
@RequestMapping("/admin/order")
@Api(tags = "订单管理模块")
public class OrderController {
    @Autowired
    OrderService orderService;

    /**
     * @description: 根据条件进行分页从查询
     * @title: getOrderPage
     * @param: [ordersPageQueryDTO]
     */
    @GetMapping("/conditionSearch")
    @ApiOperation("根据条件分页查询")
    public Result<PageResult> getOrderPage(OrdersPageQueryDTO ordersPageQueryDTO) {
        return Result.success(orderService.getOrderHistory(ordersPageQueryDTO));
    }


    /**
     * @description: 获取订单详情
     * @title: getOrderDetails
     * @param: [id]
     */
    @GetMapping("/details/{id}")
    @ApiOperation("获取订单详情")
    public Result<OrderVO> getOrderDetails(@PathVariable Long id) {
        return Result.success(orderService.getOrder(String.valueOf(id)));
    }

    /**
     * @description: 接单
     * @title: takeOrders
     * @param: [id]
     */
    @PutMapping("/confirm")
    @ApiOperation("接单")
    public Result takeOrders(@RequestBody Orders orders) {
        orderService.updateStatus(orders);
        return Result.success();
    }

    /**
     * @description: 取消订单
     * @title: cancellationOfOrder
     * @param: [orders]
     */
    @PutMapping("/cancel")
    @ApiOperation("取消订单")
    public Result cancellationOfOrder(@RequestBody OrdersCancelDTO ordersCancelDTO) {
        orderService.cancellationOfOrder(ordersCancelDTO);
        return Result.success();
    }

    /**
     * @description: 拒单
     * @title: DecliningTheOrder
     * @param: []
     */
    @PutMapping("/rejection")
    @ApiOperation("拒单")
    public Result decliningTheOrder(@RequestBody OrdersRejectionDTO ordersRejectionDTO) {
        orderService.decliningTheOrder(ordersRejectionDTO);
        return Result.success();
    }

    /**
     * @description: 通过状态查询订单数量
     * @title: getStatusCount
     * @param: []
     */
    @GetMapping("/statistics")
    @ApiOperation("通过状态查询订单数量")
    public Result<OrderStatisticsVO> getStatusCount() {
        return Result.success(orderService.getStatusCount());
    }

    /**
     * @description: 派送订单
     * @title: delivery
     * @param: [id]
     */
    @PutMapping("/delivery/{id}")
    @ApiOperation("派送订单")
    public Result delivery(@PathVariable Long id) {
        orderService.delivery(id);
        return Result.success();
    }

    /**
     * @description: 完成订单
     * @title: complete
     * @param: [id]
     */
    @PutMapping("/complete/{id}")
    @ApiOperation("完成订单")
    public Result complete(@PathVariable Long id) {
        orderService.complete(id);
        return Result.success();
    }

}
