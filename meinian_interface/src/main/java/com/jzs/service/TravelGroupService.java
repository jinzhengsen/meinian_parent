package com.jzs.service;

import com.jzs.entity.PageResult;
import com.jzs.pojo.TravelGroup;

import java.util.List;

/**
 * Description:
 * Ahthor:Jin Zhengsen
 * Date:2021/1/22  9:46
 */
public interface TravelGroupService {
    void add(TravelGroup travelGroup, Integer[] travelItemIds);

    PageResult findPage(Integer currentPage, Integer pageSize, String queryString);

    TravelGroup findGroupById(Integer id);

    List<Integer> findGroupItemIds(Integer id);

    void edit(Integer[] travelItemIds, TravelGroup travelGroup);

    List<TravelGroup> findAll();

    void delete(Integer id);
}
