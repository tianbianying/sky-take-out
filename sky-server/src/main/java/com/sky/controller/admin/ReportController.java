package com.sky.controller.admin;

import com.sky.result.Result;
import com.sky.service.ReportService;
import com.sky.vo.OrderReportVO;
import com.sky.vo.SalesTop10ReportVO;
import com.sky.vo.TurnoverReportVO;
import com.sky.vo.UserReportVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

/**
 * @description: 统计相关接口
 * @author: Excell
 * @data 2025年04月14日 10:00
 */
@RestController
@Api(tags = "统计相关接口")
@RequestMapping("/admin/report")
@Slf4j
public class ReportController {

    @Autowired
    ReportService reportService;

    /**
     * @description: 统计营业额
     * @title: turnoverStatistics
     * @param: [begin, end]
     */
    @GetMapping("/turnoverStatistics")
    @ApiOperation("统计营业额")
    public Result<TurnoverReportVO> turnoverStatistics(
            @DateTimeFormat(pattern = "yyyy-MM-dd")
            LocalDate begin,
            @DateTimeFormat(pattern = "yyyy-MM-dd")
            LocalDate end
    ) {
        return Result.success(reportService.turnoverStatistics(begin, end));
    }

    /**
     * @description: 统计用户相关接口
     * @title: userStatistics
     * @param: [begin, end]
     */
    @GetMapping("/userStatistics")
    @ApiOperation("统计用户相关接口")
    public Result<UserReportVO> userStatistics(
            @DateTimeFormat(pattern = "yyyy-MM-dd")
            LocalDate begin,
            @DateTimeFormat(pattern = "yyyy-MM-dd")
            LocalDate end
    ) {
        return Result.success(reportService.userStatistics(begin, end));
    }

    /**
     * @description: 订单统计相关接口
     * @title: ordersStatistics
     * @param: [begin, end]
     */
    @GetMapping("/ordersStatistics")
    @ApiOperation("订单统计相关接口")
    public Result<OrderReportVO> ordersStatistics(
            @DateTimeFormat(pattern = "yyyy-MM-dd")
            LocalDate begin,
            @DateTimeFormat(pattern = "yyyy-MM-dd")
            LocalDate end
    ) {
        return Result.success(reportService.ordersStatistics(begin, end));
    }

    @GetMapping("/top10")
    @ApiOperation("菜品销量排名")
    public Result<SalesTop10ReportVO> top10(
            @DateTimeFormat(pattern = "yyyy-MM-dd")
            LocalDate begin,
            @DateTimeFormat(pattern = "yyyy-MM-dd")
            LocalDate end
    ) {
        return Result.success(reportService.getTop10(begin, end));
    }
}









