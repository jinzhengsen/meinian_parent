package com.jzs.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.jzs.constant.MessageConstant;
import com.jzs.constant.RedisMessageConstant;
import com.jzs.entity.RedisConstant;
import com.jzs.entity.Result;
import com.jzs.pojo.Member;
import com.jzs.service.MemberService;
import org.springframework.beans.NotReadablePropertyException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.Map;

/**
 * Description:
 * Ahthor:Jin Zhengsen
 * Date:2021/1/30  21:56
 */
@RestController
@RequestMapping("/login")
public class LoginController {

    @Autowired
    private JedisPool jedisPool;
    @Reference
    private MemberService memberService;


    //使用手机号和验证码登录
    @RequestMapping("/check.do")
    public Result check(@RequestBody Map map, HttpServletResponse httpServletResponse){
        //获取手机号
        String telephone = map.get("telephone").toString();
        //获取验证码
        String validateCode = map.get("validateCode").toString();
        //1：从Redis中获取缓存的验证码，判断验证码输入是否正确
        String codeInRedis = jedisPool.getResource().get(telephone + RedisMessageConstant.SENDTYPE_LOGIN);
        //将输入的验证码和保存在redis中的验证码进行比较
        if (validateCode==null||codeInRedis==null||!validateCode.equals(codeInRedis)){
            //验证码输入错误
            return new Result(false, MessageConstant.VALIDATECODE_ERROR);
        }else {
            //验证码输入正确
            //2：判断当前用户是否为会员
            Member member=memberService.findMemberByTelephone(telephone);
            if (member==null){//当前用户不是会员，自动完成注册
                member=new Member();
                member.setPhoneNumber(telephone);
                member.setRegTime(new Date());
                memberService.add(member);//将注册的会员加入到数据库中
            }
            //3：:登录成功
            //写入Cookie，跟踪用户
            Cookie cookie = new Cookie("login_member_telephone",telephone);
            cookie.setPath("/");//可以在webapp文件夹下的所有应用共享cookie
            cookie.setMaxAge(60*60*24*30);//有效期30天
            httpServletResponse.addCookie(cookie);
        }
        return new Result(true,MessageConstant.LOGIN_SUCCESS);
    }
}
