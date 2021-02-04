package com.jzs.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.jzs.constant.MessageConstant;
import com.jzs.entity.PageResult;
import com.jzs.entity.QueryPageBean;
import com.jzs.entity.Result;
import com.jzs.pojo.TravelItem;
import com.jzs.service.TravelItemService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Description:
 * Ahthor:Jin Zhengsen
 * Date:2021/1/20  22:56
 */
@RestController
@RequestMapping("/travelItem")
public class traveItemController {
    @Reference
    private TravelItemService travelItemService;
    @RequestMapping("/add.do")
    @PreAuthorize("hasAuthority('TRAVELITEM_ADD')")
    public Result add(@RequestBody TravelItem travelItem){
        try {
            travelItemService.add(travelItem);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.ADD_TRAVELITEM_FAIL);
        }
        return new Result(true,MessageConstant.ADD_TRAVELITEM_SUCCESS);
    }

    @RequestMapping("/findPage.do")
    @PreAuthorize("hasAuthority('TRAVELITEM_QUERY')")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean){
        PageResult pageResult=travelItemService.findPage(queryPageBean);
        return pageResult;
    }

    @RequestMapping("/delete.do")
    @PreAuthorize("hasAuthority('ABC')")
    public Result delete(Integer id){
        try {
            travelItemService.delete(id);
            return new Result(true,MessageConstant.DELETE_TRAVELITEM_SUCCESS);
        } catch (Exception e) {
            return new Result(false,MessageConstant.DELETE_TRAVELITEM_FAIL);
        }
    }

    @RequestMapping("/findById.do")
    public Result findById(Integer id){
        TravelItem travelItem=travelItemService.findById(id);
        return new Result(true,MessageConstant.QUERY_TRAVELITEM_SUCCESS,travelItem);
    }

    @RequestMapping("/edit.do")
    public Result edit(@RequestBody TravelItem travelItem){
        travelItemService.edit(travelItem);
        return new Result(true,MessageConstant.EDIT_TRAVELITEM_SUCCESS);
    }

    @RequestMapping("/findAll.do")
    public Result findAll(){
        System.out.println("进入findAll.....");
        List<TravelItem> travelItemList=travelItemService.findAll();
        System.out.println(".......");
        return new Result(true,MessageConstant.QUERY_TRAVELGROUP_SUCCESS,travelItemList);
    }

}
