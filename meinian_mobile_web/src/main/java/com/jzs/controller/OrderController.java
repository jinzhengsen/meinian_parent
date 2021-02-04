package com.jzs.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.jzs.constant.MessageConstant;
import com.jzs.constant.RedisMessageConstant;
import com.jzs.entity.Result;
import com.jzs.pojo.Order;
import com.jzs.service.OrderService;
import com.jzs.util.SMSUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

import java.util.Map;

/**
 * Description:
 * Ahthor:Jin Zhengsen
 * Date:2021/1/29  23:11
 */
@RestController
@RequestMapping("/order")
public class OrderController {
    @Autowired
    JedisPool jedisPool;
    @Reference
    OrderService orderService;

    @RequestMapping("/submit.do")
    public Result submit(@RequestBody Map map){
        // ① 在页面获取手机号
         String telephone= map.get("telephone").toString();
        // ② 在页面获取验证码
        String validateCode=map.get("validateCode").toString();
        //从Redis中获取缓存的验证码，key为手机号+RedisConstant.SENDTYPE_ORDER
        String codeInRedis=jedisPool.getResource().get(telephone+ RedisMessageConstant.SENDTYPE_ORDER);

        //校验手机验证码
        if (validateCode==null||codeInRedis==null||!codeInRedis.equals(validateCode)){
            return new Result(false, MessageConstant.VALIDATECODE_ERROR);
        }

        Result result=null;
        //调用旅游预约服务
        try {
            map.put("orderType", Order.ORDERTYPE_WEIXIN);
            // 断点调试，查看map里面封装了哪些数据
            result=orderService.order(map);
        } catch (Exception e) {
            e.printStackTrace();
            //预约失败
            return result;
        }
        if (result.isFlag()){
            //预约成功，发送短信通知，短信通知内容可以是“预约时间”，“预约人”，“预约地点”，“预约事项”等信息。
            String orderDate = map.get("orderDate").toString();
            try {
                SMSUtils.sendShortMessage(telephone,orderDate);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    @RequestMapping("/findById.do")
    public Result findById(Integer id) throws Exception {
        System.out.println("进入controller中findById的方法");
        try {
            Map map=orderService.findById(id);
            System.out.println("map===>>>"+map);
            //查询预约信息成功
            return new Result(true,MessageConstant.QUERY_ORDER_SUCCESS,map);
        } catch (Exception e) {
            e.printStackTrace();
            //查询预约信息失败
            return new Result(false,MessageConstant.QUERY_ORDER_FAIL);
        }
    }

}
