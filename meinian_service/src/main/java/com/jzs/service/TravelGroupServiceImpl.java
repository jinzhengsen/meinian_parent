package com.jzs.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.jzs.dao.TravelGroupDao;
import com.jzs.entity.PageResult;
import com.jzs.pojo.TravelGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Description:
 * Ahthor:Jin Zhengsen
 * Date:2021/1/22  9:46
 */
@Service(interfaceClass = TravelGroupService.class)
@Transactional
public class TravelGroupServiceImpl implements TravelGroupService{
    @Autowired
    TravelGroupDao travelGroupDao;
    @Override
    public void add(TravelGroup travelGroup, Integer[] travelItemIds) {
// 1 新增跟团游，想t_travelgroup中添加数据，新增后返回新增的id
        travelGroupDao.add(travelGroup);
        // 2 新增跟团游和自由行中间表t_travelgroup_travelitem新增数据(新增几条，由travelItemIds决定)
        setTravelGroupAndTravelItem(travelGroup.getId(),travelItemIds);
    }

    @Override
    public PageResult findPage(Integer currentPage, Integer pageSize, String queryString) {
        // 使用分页插件PageHelper，设置当前页，每页最多显示的记录数
        PageHelper.startPage(currentPage,pageSize);
        // 响应分页插件的Page对象
        Page<TravelGroup> page= travelGroupDao.findPage(queryString);
        return new PageResult(page.getTotal(),page.getResult());
    }

    @Override
    public TravelGroup findGroupById(Integer id) {
        return travelGroupDao.findGroupById(id);
    }

    @Override
    public List<Integer> findGroupItemIds(Integer id) {
         return travelGroupDao.findGroupItemIds(id);
    }

    @Override
    public void edit(Integer[] travelItemIds, TravelGroup travelGroup) {
        //修改跟团游
        travelGroupDao.edit(travelGroup);
        //根据跟团游id删除关系表数据
        travelGroupDao.delete(travelGroup.getId());
        //添加关系表数据
        setTravelGroupAndTravelItem(travelGroup.getId(),travelItemIds);
    }

    @Override
    public List<TravelGroup> findAll() {
        return travelGroupDao.findAll();
    }

    @Override
    public void delete(Integer id) {
        // 在删除报团游之前，先判断报团游的id，在中间表中是否存在数据(在t_setmeal_travelgroup中查找是否有关联关系)
        //如果要删除的报团游在t_setmeal_travelgroup中被添加到了套餐中就不允许删除
        long count=travelGroupDao.findCountByTravelGroupItemId(id);
        if (count>0){
            throw new RuntimeException("此跟团游已被加入套餐中，不允许删除!");
        }

        // 使用报团游的id进行删除(删除的是t_travelgroup_travelitem表中的数据)
        travelGroupDao.deleteGroupsById(id);

        // 使用报团游的id进行删除(删除的是t_travelgroup表中的数据)
        travelGroupDao.deleteById(id);


    }

    private void setTravelGroupAndTravelItem(Integer travelGroupId,Integer[] travelItemIds){
        // 新增几条数据，由travelItemIds的长度决定
        if (travelItemIds!=null&&travelItemIds.length>0){
            for (Integer travelItemId : travelItemIds) {
                // 如果有多个参数使用map
                Map<String,Integer> map=new HashMap<>();
                map.put("travelGroupId",travelGroupId);
                map.put("travelItemId",travelItemId);
                travelGroupDao.setTravelGroupAndTravelItem(map);
            }
        }
    }


}
