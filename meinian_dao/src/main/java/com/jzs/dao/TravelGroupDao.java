package com.jzs.dao;

import com.github.pagehelper.Page;
import com.jzs.pojo.TravelGroup;

import java.util.List;
import java.util.Map;

/**
 * Description:
 * Ahthor:Jin Zhengsen
 * Date:2021/1/22  9:48
 */
public interface TravelGroupDao {
    void add(TravelGroup travelGroup);

    void setTravelGroupAndTravelItem(Map<String, Integer> map);

    Page<TravelGroup> findPage(String queryString);

    TravelGroup findGroupById(Integer id);

    List<Integer> findGroupItemIds(Integer id);

    void edit(TravelGroup travelGroup);

    void delete(Integer id);

    List<TravelGroup> findAll();

    long findCountByTravelGroupItemId(Integer id);

    void deleteById(Integer id);

    void deleteGroupsById(Integer id);

    List<TravelGroup> findTravelGroupListById(Integer id);
}
