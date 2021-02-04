package com.jzs.dao;

import com.github.pagehelper.Page;
import com.jzs.pojo.TravelItem;

import java.util.List;

/**
 * Description:
 * Ahthor:Jin Zhengsen
 * Date:2021/1/20  23:08
 */
public interface TravelItemDao {
    public void add(TravelItem travelItem);

    Page findPage(String queryString);

    long findCountByTravelItemItemId(Integer id);

    void deleteById(Integer id);

    TravelItem findById(Integer id);

    void edit(TravelItem travelItem);

    List<TravelItem> findAll();

    List<TravelItem> findTravelItemListById(Integer id);
}
