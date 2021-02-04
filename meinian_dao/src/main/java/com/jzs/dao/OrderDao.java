package com.jzs.dao;

import com.jzs.pojo.Order;

import java.util.List;
import java.util.Map;

/**
 * Description:
 * Ahthor:Jin Zhengsen
 * Date:2021/1/30  0:07
 */
public interface OrderDao {

    // 根据预约信息查询是否已经预约
    List<Order> findOrder(Order order);

    void add(Order order);

    Map findById(Integer id);

    int getTodayOrderNumber(String today);

    int getTodayVisitsNumber(String today);

    int getThisWeekAndMonthOrderNumber(Map<String, Object> paramWeek);

    int getThisWeekAndMonthVisitsNumber(Map<String, Object> paramWeekVisit);

    List<Map<String, Object>> findHotSetmeal();
}
