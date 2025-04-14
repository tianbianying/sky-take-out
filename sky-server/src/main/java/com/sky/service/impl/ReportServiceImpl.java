package com.sky.service.impl;

import com.sky.dto.GoodsSalesDTO;
import com.sky.entity.Orders;
import com.sky.mapper.OrderMapper;
import com.sky.mapper.ReportMapper;
import com.sky.service.ReportService;
import com.sky.vo.OrderReportVO;
import com.sky.vo.SalesTop10ReportVO;
import com.sky.vo.TurnoverReportVO;
import com.sky.vo.UserReportVO;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @description: 统计接口的服务类
 * @author: Excell
 * @date: 2025年04月14日 10:07
 */
@Service
public class ReportServiceImpl implements ReportService {

    @Autowired
    private ReportMapper reportMapper;
    @Autowired
    private OrderMapper orderMapper;

    /**
     * 统计营业额
     */
    @Override
    public TurnoverReportVO turnoverStatistics(LocalDate begin, LocalDate end) {
        List<LocalDate> dateList = new ArrayList<>();
        List<Double> turnoverList = new ArrayList<>();

        while (!begin.isAfter(end)) {
            dateList.add(begin);

            Map<String, Object> param = createTimeRangeParam(begin);
            param.put("status", Orders.COMPLETED);

            Double turnover = reportMapper.getTurnover(param);
            turnoverList.add(turnover == null ? 0.0 : turnover);

            begin = begin.plusDays(1);
        }

        return TurnoverReportVO.builder()
                .dateList(StringUtils.join(dateList, ","))
                .turnoverList(StringUtils.join(turnoverList, ","))
                .build();
    }

    /**
     * 统计用户数据
     */
    @Override
    public UserReportVO userStatistics(LocalDate begin, LocalDate end) {
        List<LocalDate> dateList = new ArrayList<>();
        List<Integer> newUserList = new ArrayList<>();
        List<Integer> totalUserList = new ArrayList<>();

        while (!begin.isAfter(end)) {
            dateList.add(begin);

            // 1. 总用户数（截止到当天）
            Map<String, Object> totalMap = new HashMap<>();
            totalMap.put("end", LocalDateTime.of(begin, LocalTime.MAX));
            Integer total = reportMapper.getUserCount(totalMap);
            totalUserList.add(total == null ? 0 : total);

            // 2. 新增用户数（当天范围）
            Map<String, Object> newUserMap = createTimeRangeParam(begin);
            Integer newUser = reportMapper.getUserCount(newUserMap);
            newUserList.add(newUser == null ? 0 : newUser);

            begin = begin.plusDays(1);
        }

        return UserReportVO.builder()
                .dateList(StringUtils.join(dateList, ","))
                .newUserList(StringUtils.join(newUserList, ","))
                .totalUserList(StringUtils.join(totalUserList, ","))
                .build();
    }

    @Override
    public OrderReportVO ordersStatistics(LocalDate begin, LocalDate end) {
        List<LocalDate> dateList = new ArrayList<>();
        List<Integer> orderCountList = new ArrayList<>();
        List<Integer> validOrderCountList = new ArrayList<>();

        int totalOrderCount = 0;
        int totalValidOrderCount = 0;

        while (!begin.isAfter(end)) {
            dateList.add(begin);

            Map<String, Object> timeRangeMap = createTimeRangeParam(begin);

            // 获取当天总订单数
            int orderCount = getSafeOrderCount(timeRangeMap);
            orderCountList.add(orderCount);
            totalOrderCount += orderCount;

            // 获取当天已完成订单数
            timeRangeMap.put("status", Orders.COMPLETED);
            int validOrderCount = getSafeOrderCount(timeRangeMap);
            validOrderCountList.add(validOrderCount);
            totalValidOrderCount += validOrderCount;

            begin = begin.plusDays(1);
        }

        double orderCompletionRate = totalOrderCount == 0 ? 0.0 :
                (double) totalValidOrderCount / totalOrderCount;

        return OrderReportVO.builder()
                .dateList(StringUtils.join(dateList, ","))
                .orderCountList(StringUtils.join(orderCountList, ","))
                .validOrderCountList(StringUtils.join(validOrderCountList, ","))
                .totalOrderCount(totalOrderCount)
                .validOrderCount(totalValidOrderCount)
                .orderCompletionRate(orderCompletionRate)
                .build();
    }

    /**
     * @description: 获取销量排名
     * @title: getTop10
     * @param: [begin, end]
     */
    @Override
    public SalesTop10ReportVO getTop10(LocalDate begin, LocalDate end) {
        List<GoodsSalesDTO> goodsSalesDTOS = reportMapper
                .getTop10(LocalDateTime.of(begin, LocalTime.MIN), LocalDateTime.of(end, LocalTime.MAX), Orders.COMPLETED);

        // 获取菜品名称集合
        List<String> goodsSalesDTONames = goodsSalesDTOS.stream()
                .map(GoodsSalesDTO::getName)
                .collect(Collectors.toList());
        // 获取销量
        List<Integer> goodsSalesDTONumbers = goodsSalesDTOS.stream()
                .map(GoodsSalesDTO::getNumber)
                .collect(Collectors.toList());


        return SalesTop10ReportVO
                .builder()
                .nameList(StringUtils.join(goodsSalesDTONames, ","))
                .numberList(StringUtils.join(goodsSalesDTONumbers, ","))
                .build();
    }

    // 防空判断封装
    private int getSafeOrderCount(Map<String, Object> conditionMap) {
        Integer result = reportMapper.getOrderCount(conditionMap);
        return result != null ? result : 0;
    }


    /**
     * 生成某一天的时间区间参数 Map（00:00 ~ 23:59:59）
     */
    private Map<String, Object> createTimeRangeParam(LocalDate date) {
        Map<String, Object> param = new HashMap<>();
        param.put("begin", LocalDateTime.of(date, LocalTime.MIN));
        param.put("end", LocalDateTime.of(date, LocalTime.MAX));
        return param;
    }
}
