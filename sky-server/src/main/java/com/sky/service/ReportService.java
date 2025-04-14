package com.sky.service;

import com.sky.vo.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @description: 统计接口的服务层接口
 * @author: Excell
 * @date: 2025年04月14日 10:07
 */
public interface ReportService {
    /**
     * @description: 统计营业额接口
     * @title: turnoverStatistics
     * @param: [begin, end]
     */
    TurnoverReportVO turnoverStatistics(LocalDate begin, LocalDate end);

    /**
     * @description: 统计用户相关接口
     * @title: userStatistics
     * @param: [begin, end]
     */
    UserReportVO userStatistics(LocalDate begin, LocalDate end);

    /**
     * @description: 订单统计相关接口
     * @title: ordersStatistics
     * @param: [begin, end]
     */
    OrderReportVO ordersStatistics(LocalDate begin, LocalDate end);

    /**
     * @description: 获取销量排名
     * @title: getTop10
     * @param: [begin, end]
     */
    SalesTop10ReportVO getTop10(LocalDate begin, LocalDate end);
}
