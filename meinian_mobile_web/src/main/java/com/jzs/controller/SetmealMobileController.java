package com.jzs.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.jzs.constant.MessageConstant;
import com.jzs.entity.Result;
import com.jzs.pojo.Setmeal;
import com.jzs.service.SetmealService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Description:
 * Ahthor:Jin Zhengsen
 * Date:2021/1/27  0:01
 */
@RestController
@RequestMapping("/setmeal")
public class SetmealMobileController {
    @Reference
    SetmealService setmealService;

    /**
     * 查询所有套餐信息
     * @return
     */
    @RequestMapping("/getSetmeal.do")
    public Result getSetmeal(){
        List<Setmeal> setmealList =setmealService.findAll();
        return new Result(true, MessageConstant.QUERY_SETMEALLIST_SUCCESS,setmealList);
    }

    /**
     * 查询具体套餐信息
     * @param id
     * @return
     */
    @RequestMapping("/findById.do")
    public Result findById(Integer id){
        try {
            Setmeal setmeal=setmealService.findById(id);
            System.out.println("setmeal==>"+setmeal);
            return new Result(true,MessageConstant.QUERY_SETMEAL_SUCCESS,setmeal);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.QUERY_SETMEAL_FAIL);
        }
    }

    @RequestMapping("/getSetmealById.do")
    public Result getSetmealById(Integer id){
        Setmeal setmeal=setmealService.getSetmealById(id);
        if (setmeal==null){
            return new Result(false,MessageConstant.QUERY_SETMEAL_FAIL);
        }
        return new Result(true,MessageConstant.QUERY_SETMEAL_SUCCESS,setmeal);
    }
}
