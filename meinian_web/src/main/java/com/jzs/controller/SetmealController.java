package com.jzs.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.jzs.constant.MessageConstant;
import com.jzs.constant.RedisConstant;
import com.jzs.entity.PageResult;
import com.jzs.entity.QiniuUtils;
import com.jzs.entity.QueryPageBean;
import com.jzs.entity.Result;
import com.jzs.pojo.Setmeal;
import com.jzs.pojo.TravelGroup;
import com.jzs.service.SetmealService;
import org.apache.zookeeper.data.Id;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import redis.clients.jedis.JedisPool;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

/**
 * Description:
 * Ahthor:Jin Zhengsen
 * Date:2021/1/23  10:38
 */

@RestController
@RequestMapping("/setmeal")
public class SetmealController {

    @Reference
    SetmealService setmealService;

    @Autowired
    JedisPool jedisPool;

    @RequestMapping("/upload.do")
    public Result upload(MultipartFile imgFile) throws IOException {
//        System.out.println("进入upload。。。。。");
        try {
            //获取原始文件名
            String originalFilename = imgFile.getOriginalFilename();
            // 找到.最后出现的位置
            int lastIndexOf = originalFilename.lastIndexOf(".");
            //获取文件后缀
            String suffix=originalFilename.substring(lastIndexOf);
            //使用UUID随机产生文件名称，防止同名文件覆盖
            String fileName = UUID.randomUUID().toString() + suffix;
            QiniuUtils.upload2Qiniu(imgFile.getBytes(),fileName);

            //将上传图片名称存入Redis，基于Redis的Set集合存储
            jedisPool.getResource().sadd(RedisConstant.SETMEAL_PIC_RESOURCES,fileName);
            //图片上传成功
            return new Result(true, MessageConstant.PIC_UPLOAD_SUCCESS,fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new Result(false, MessageConstant.PIC_UPLOAD_FAIL);
    }

    @RequestMapping("/add.do")
    public Result add(Integer[] travelgroupIds,@RequestBody Setmeal setmeal){
        try {
            setmealService.add(travelgroupIds,setmeal);
        } catch (Exception e) {
            return new Result(false,MessageConstant.ADD_SETMEAL_FAIL);
        }
        return new Result(true,MessageConstant.ADD_SETMEAL_SUCCESS);
    }


    @RequestMapping("/findPage.do")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean){
        PageResult pageResult=setmealService.findPage(
                queryPageBean.getCurrentPage(),
                queryPageBean.getPageSize(),
                queryPageBean.getQueryString());
        return pageResult;
    }

    @RequestMapping("/delete.do")
    public Result delete(Integer id){
        try {
            setmealService.delete(id);
            return new Result(true,"删除套餐成功");
        } catch (Exception e) {
            return new Result(false,"删除套餐失败");
        }
    }

    @RequestMapping("/findSetmealById.do")
    public Result findSetmealById(Integer id){
       Setmeal setmeal= setmealService.findSetmealById(id);
        return new Result(true,MessageConstant.QUERY_SETMEAL_SUCCESS,setmeal);
    }

    @RequestMapping("/findGroupIds.do")
    public Result findGroupIds(Integer id){
        try {
            List<Integer> groupIds= setmealService.findGroupIds(id);
            return new Result(true,"查询套餐关联报团游信息成功",groupIds);
        } catch (Exception e) {
            return new Result(false,"查询套餐关联报团游信息失败");
        }
    }

    @RequestMapping("/edit.do")
    public Result edit(Integer[] travelgroupIds,@RequestBody Setmeal setmeal){
        System.out.println("进入controller中的edit方法");
        try {
            setmealService.edit(travelgroupIds,setmeal);
            return new Result(true,"编辑套餐成功");
        } catch (Exception e) {
            return new Result(false,"编辑套餐失败");
        }
    }

}
