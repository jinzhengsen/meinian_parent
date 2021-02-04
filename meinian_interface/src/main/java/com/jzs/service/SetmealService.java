package com.jzs.service;

import com.jzs.entity.PageResult;
import com.jzs.pojo.Setmeal;

import java.util.List;
import java.util.Map;

/**
 * Description:
 * Ahthor:Jin Zhengsen
 * Date:2021/1/23  10:40
 */
public interface SetmealService {
    void add(Integer[] travelgroupIds, Setmeal setmeal);

    PageResult findPage(Integer currentPage, Integer pageSize, String queryString);

    void delete(Integer id);

    Setmeal findSetmealById(Integer id);

    List<Integer> findGroupIds(Integer id);

    void edit(Integer[] travelgroupIds, Setmeal setmeal);

    List<Setmeal> findAll();

    Setmeal findById(Integer id);

    Setmeal getSetmealById(Integer id);

    List<Map<String, Object>> findSetmealCount();

}
