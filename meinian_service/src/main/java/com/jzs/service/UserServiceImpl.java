package com.jzs.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.jzs.dao.UserDao;
import com.jzs.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

/**
 * Description:
 * Ahthor:Jin Zhengsen
 * Date:2021/1/31  23:02
 */
@Service(interfaceClass = UserService.class)
@Transactional
public class UserServiceImpl implements UserService{
    @Autowired
    UserDao userDao;
    @Override
    public User findUserByUsername(String username) {
        return userDao.findUserByUsername(username);
    }
}
