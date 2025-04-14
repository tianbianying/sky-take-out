package com.sky.mapper;

import com.sky.dto.GoodsSalesDTO;
import org.apache.ibatis.annotations.Mapper;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * @description: 统计接口的持久层
 * @author: Excell
 * @date: 2025年04月14日 10:56
 */
@Mapper
public interface ReportMapper {
    /**
     * @description: 根据条件查询营业额
     * @title: getTurnover
     * @param: [map]
     */
    Double getTurnover(Map map);

    /**
     * @description: 根据条件查询用户
     * @title: getUserCount
     * @param: [map]
     */
    Integer getUserCount(Map map);

    /**
     * @description: 根据条件查询订单
     * @title: getOrderCount
     * @param: [conditionMap]
     */
    Integer getOrderCount(Map<String, Object> conditionMap);

    /**
     * @description: 根据条件获取销量排名
     * @title: getTop10
     * @param: [begin, end, completed]
     */
    List<GoodsSalesDTO> getTop10(LocalDateTime begin, LocalDateTime end, Integer status);
}
