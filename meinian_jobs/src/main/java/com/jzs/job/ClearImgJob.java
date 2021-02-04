package com.jzs.job;

import com.jzs.constant.RedisConstant;
import com.jzs.entity.QiniuUtils;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.JedisPool;

import java.util.Set;

/**
 * Description:
 * Ahthor:Jin Zhengsen
 * Date:2021/1/28  18:51
 */
public class ClearImgJob {
    @Autowired
    JedisPool jedisPool;
    //删除redis中的图片名字  删除七牛云图片名字
    public void clearImg(){
        //计算七牛云和mysql中的差值
        Set<String> sdiff = jedisPool.getResource().sdiff(RedisConstant.SETMEAL_PIC_RESOURCES, RedisConstant.SETMEAL_PIC_DB_RESOURCES);
        for (String picName : sdiff) {
            //删除redis
            jedisPool.getResource().srem(RedisConstant.SETMEAL_PIC_RESOURCES,picName);
            //删除七牛云
            QiniuUtils.deleteFileFromQiniu(picName);
        }
    }
}
