package com.jzs.service;

import com.jzs.entity.PageResult;
import com.jzs.entity.QueryPageBean;
import com.jzs.pojo.TravelItem;

import java.util.List;

/**
 * Description:
 * Ahthor:Jin Zhengsen
 * Date:2021/1/20  23:04
 */
public interface TravelItemService {
    public void add(TravelItem travelItem);
    PageResult findPage(QueryPageBean queryPageBean);

    void delete(Integer id);

    TravelItem findById(Integer id);

    void edit(TravelItem travelItem);

    List<TravelItem> findAll();
}
