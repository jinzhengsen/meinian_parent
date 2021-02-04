package com.jzs.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.jzs.constant.RedisConstant;
import com.jzs.dao.SetmealDao;
import com.jzs.entity.PageResult;
import com.jzs.pojo.Setmeal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.JedisPool;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Description:
 * Ahthor:Jin Zhengsen
 * Date:2021/1/23  10:40
 */
@Service(interfaceClass = SetmealService.class)
@Transactional
public class SetmealServiceImpl implements SetmealService{
    @Autowired
    SetmealDao setmealDao;

    @Autowired
    JedisPool jedisPool;
    @Override
    public void add(Integer[] travelgroupIds, Setmeal setmeal) {
        System.out.println("setmeal.getImg()==>"+setmeal.getImg());
        //新增套餐
        setmealDao.add(setmeal);
        // 2：向套餐和跟团游的中间表中插入数据
        if (travelgroupIds!=null&&travelgroupIds.length>0){
            //绑定套餐和跟团游的多对多关系
            setSetmealAndTravelGroup(setmeal.getId(),travelgroupIds);
    }

        //将图片名称保存到Redis
        String img = setmeal.getImg();
        if (img != null) {
            savePic2Redis(img);
        }

    }

    @Override
    public PageResult findPage(Integer currentPage, Integer pageSize, String queryString) {
       PageHelper.startPage(currentPage, pageSize);
       Page<Setmeal> page=setmealDao.findPage(queryString);
       return new PageResult(page.getTotal(),page.getResult());
    }

    @Override
    public void delete(Integer id) {
        //删除套餐报团游表
        setmealDao.deleteSetMealGroup(id);
        //删除套餐表
        setmealDao.delete(id);

    }

    @Override
    public Setmeal findSetmealById(Integer id) {
        return setmealDao.findSetmealById(id);
    }

    @Override
    public List<Integer> findGroupIds(Integer id) {
        return setmealDao.findGroupIds(id);
    }

    @Override
    public void edit(Integer[] travelgroupIds, Setmeal setmeal) {
        //修改套餐
        setmealDao.edit(setmeal);
        //根据套餐id删除关系表数据
        setmealDao.deleteSetmealGroupById(setmeal.getId());
        //添加关系表数据
        setSetmealAndTravelGroup(setmeal.getId(),travelgroupIds);
        //将图片名称保存到Redis
        String img = setmeal.getImg();
        System.out.println("img==>"+img);
        if (img!=null){
            savePic2Redis(img);
        }
    }

    @Override
    public List<Setmeal> findAll() {
        return setmealDao.findAll();
    }

    //根据套餐id查询具体套餐信息
    @Override
    public Setmeal findById(Integer id) {
        System.out.println("service。。。。");
        return setmealDao.findById(id);
    }

    @Override
    public Setmeal getSetmealById(Integer id) {
        return setmealDao.getSetmealById(id);
    }

    @Override
    public List<Map<String, Object>> findSetmealCount() {
        return setmealDao.findSetmealCount();
    }

    //将图片名称保存到Redis
    private void savePic2Redis(String pic){
        jedisPool.getResource().sadd(RedisConstant.SETMEAL_PIC_DB_RESOURCES,pic);
    }

    //绑定套餐和跟团游的多对多关系
    public void setSetmealAndTravelGroup(Integer setmealId,Integer[] travelgroupIds){
        for (Integer travelgroupId : travelgroupIds) {
            Map<String,Integer> map=new HashMap<>();
            map.put("travelgroupId",travelgroupId);
            map.put("setmealId",setmealId);
            setmealDao.setSetmealAndTravelGroup(map);
        }
    }
}
