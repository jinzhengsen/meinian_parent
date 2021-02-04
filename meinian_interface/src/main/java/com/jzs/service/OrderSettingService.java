package com.jzs.service;

import com.jzs.pojo.OrderSetting;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Description:
 * Ahthor:Jin Zhengsen
 * Date:2021/1/27  22:39
 */
public interface OrderSettingService {
    void add(List<OrderSetting> orderSettings);

    List<Map> getOrderSettingByMonth(String date);

    void editNumberByDate(OrderSetting orderSetting);
}
