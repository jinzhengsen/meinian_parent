package com.jzs.controller;

import com.jzs.constant.MessageConstant;
import com.jzs.constant.RedisMessageConstant;
import com.jzs.entity.Result;
import com.jzs.util.SMSUtils;
import com.jzs.util.ValidateCodeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

/**
 * Description:
 * Ahthor:Jin Zhengsen
 * Date:2021/1/29  20:59
 */
@RestController
@RequestMapping("/validateCode")
public class ValidateCodeController {

    @Autowired
    JedisPool jedisPool;
    @RequestMapping("/send4Order.do")
    public Result send4Order(String telephone){
        //生成4位随机数的验证码
        Integer code = ValidateCodeUtils.generateValidateCode(4);
        try {
            //发送短信
            SMSUtils.sendShortMessage(telephone,code.toString());
        } catch (Exception e) {
            e.printStackTrace();
            //发送验证码失败
            return new Result(false, MessageConstant.SEND_VALIDATECODE_FAIL);
        }
        System.out.println("发送的手机验证码为："+code);
        //将生成的验证码保存到redis中
        jedisPool.getResource().setex(telephone+ RedisMessageConstant.SENDTYPE_ORDER,5*60,code.toString());
        return new Result(true,MessageConstant.SEND_VALIDATECODE_SUCCESS);
    }

    @RequestMapping("/send4Login.do")
    public Result send4Login(String telephone){
        //生成4位数字验证码
        Integer code = ValidateCodeUtils.generateValidateCode(4);
        try {
            //发送验证码短信
            SMSUtils.sendShortMessage(telephone,code.toString());
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.SEND_VALIDATECODE_FAIL);
        }
        //将验证码保存到redis数据库中
        jedisPool.getResource().setex(telephone+RedisMessageConstant.SENDTYPE_LOGIN,5*60,code.toString());
        //验证码发送成功
        return new Result(true,MessageConstant.SEND_VALIDATECODE_SUCCESS);
    }
}
