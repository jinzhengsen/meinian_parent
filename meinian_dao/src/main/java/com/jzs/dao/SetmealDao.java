package com.jzs.dao;

import com.github.pagehelper.Page;
import com.jzs.pojo.Setmeal;

import java.util.List;
import java.util.Map;

/**
 * Description:
 * Ahthor:Jin Zhengsen
 * Date:2021/1/23  10:41
 */

public interface SetmealDao {
    void add(Setmeal setmeal);
    void setSetmealAndTravelGroup(Map<String, Integer> map);

    Page<Setmeal> findPage(String queryString);

    void deleteSetMealGroup(Integer id);

    void delete(Integer id);

    Setmeal findSetmealById(Integer id);

    List<Integer> findGroupIds(Integer id);

    void edit(Setmeal setmeal);

    List<Setmeal> findAll();

    void deleteSetmealGroupById(Integer id);

    Setmeal findById(Integer id);

    Setmeal getSetmealById(Integer id);

    List<Map<String, Object>> findSetmealCount();

}
