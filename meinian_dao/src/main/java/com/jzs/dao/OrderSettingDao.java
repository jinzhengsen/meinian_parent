package com.jzs.dao;

import com.jzs.pojo.OrderSetting;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * Description:
 * Ahthor:Jin Zhengsen
 * Date:2021/1/27  22:40
 */
public interface OrderSettingDao {
    long findCountByOrderDate(Date orderDate);

    void editNumberByOrderDate(OrderSetting orderSetting);

    void add(OrderSetting orderSetting);

    List<OrderSetting> getOrderSettingByMonth(@Param("date") String date);

    //根据预约日期查询预约设置信息
    OrderSetting findOrderSettingByOrderDate(Date date);

    //更新已预约人数
    void editReservationsByOrderDate(OrderSetting orderSetting);
}
