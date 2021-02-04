package com.jzs.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.jzs.dao.TravelItemDao;
import com.jzs.entity.PageResult;
import com.jzs.entity.QueryPageBean;
import com.jzs.pojo.TravelItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Description:
 * Ahthor:Jin Zhengsen
 * Date:2021/1/20  23:06
 */
@Service(interfaceClass = TravelItemService.class)
@Transactional
public class TravelItemServiceImpl implements TravelItemService {
    @Autowired
    private TravelItemDao travelItemDao;
    @Override
    public void add(TravelItem travelItem) {
        travelItemDao.add(travelItem);
    }

    @Override
    public PageResult findPage(QueryPageBean queryPageBean) {
        //1、初始化分页操作
        PageHelper.startPage(queryPageBean.getCurrentPage(),queryPageBean.getPageSize());//limit
        //2、使用sql语句进行查询（不必在使用mysql的limit了）
        Page page=travelItemDao.findPage(queryPageBean.getQueryString());
        //3、封装
        return new PageResult(page.getTotal(),page.getResult());
    }

    @Override
    public void delete(Integer id) {
//        System.out.println("进入service中的delete方法......");
        // 在删除自由行之前，先判断自由行的id，在中间表中是否存在数据
        long count=travelItemDao.findCountByTravelItemItemId(id);
        // 中间表如果有数据，不要往后面执行，直接抛出异常
        // 如果非要删除也可以：delete from t_travelgroup_travelitem where travelitem_id = 1
        if (count>0){
            throw new RuntimeException("不允许删除");
        }
        // 使用自由行的id进行删除
        travelItemDao.deleteById(id);
    }

    @Override
    public TravelItem findById(Integer id) {
        return travelItemDao.findById(id);
    }

    @Override
    public void edit(TravelItem travelItem) {
        travelItemDao.edit(travelItem);
    }

    @Override
    public List<TravelItem> findAll() {
        return travelItemDao.findAll();
    }
}
