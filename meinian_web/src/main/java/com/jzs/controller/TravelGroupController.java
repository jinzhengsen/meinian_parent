package com.jzs.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.jzs.constant.MessageConstant;
import com.jzs.entity.PageResult;
import com.jzs.entity.QueryPageBean;
import com.jzs.entity.Result;
import com.jzs.pojo.TravelGroup;
import com.jzs.service.TravelGroupService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Description:
 * Ahthor:Jin Zhengsen
 * Date:2021/1/22  9:43
 */
@RequestMapping("/travelgroup")
@RestController
public class TravelGroupController {
    @Reference
    TravelGroupService travelGroupService;

    @RequestMapping("/add.do")
    public Result add(@RequestBody TravelGroup travelGroup, Integer[] travelItemIds){
        travelGroupService.add(travelGroup,travelItemIds);
        return new Result(true, MessageConstant.ADD_TRAVELGROUP_SUCCESS);
    }

    // 传递当前页，每页显示的记录数，查询条件
    // 响应PageResult，封装总记录数，结果集
    @RequestMapping("/findPage.do")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean){
        PageResult pageResult=travelGroupService.findPage(queryPageBean.getCurrentPage(),queryPageBean.getPageSize(),queryPageBean.getQueryString());
        return pageResult;
    }

    @RequestMapping("/findGroupById.do")
    public Result findGroupById(Integer id){
        TravelGroup travelGroup=travelGroupService.findGroupById(id);
        return new Result(true,MessageConstant.QUERY_TRAVELGROUP_SUCCESS,travelGroup);
    }

    @RequestMapping("/findGroupItemIds.do")
    public Result findGroupItemIds(Integer id){
        List<Integer> travelItemIds=travelGroupService.findGroupItemIds(id);
        return new Result(true,"自由行关联id查询成功",travelItemIds);
    }

    @RequestMapping("/edit.do")
    public Result edit(Integer[] travelItemIds,@RequestBody TravelGroup travelGroup){
        try {
            travelGroupService.edit(travelItemIds,travelGroup);
        } catch (Exception e) {
            return new Result(false,MessageConstant.EDIT_TRAVELGROUP_FAIL);
        }
        return new Result(true,MessageConstant.EDIT_TRAVELGROUP_SUCCESS);
    }

    @RequestMapping("/findAll.do")
    public Result findAll(){
        //查询所有的跟团游信息
        List<TravelGroup> groupLists=travelGroupService.findAll();
        if (groupLists!=null&&groupLists.size()>0){
            return new Result(true,MessageConstant.QUERY_TRAVELGROUP_SUCCESS,groupLists);
        }
        return new Result(false,MessageConstant.QUERY_TRAVELGROUP_FAIL);

    }

    @RequestMapping("/delete.do")
    public Result delete(Integer id){
        try {
            travelGroupService.delete(id);
            return new Result(true,MessageConstant.DELETE_TRAVELGROUP_SUCCESS);
        } catch (Exception e) {
            return new Result(false,MessageConstant.DELETE_TRAVELGROUP_FAIL);
        }
    }

}
