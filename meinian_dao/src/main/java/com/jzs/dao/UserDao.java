package com.jzs.dao;

import com.jzs.pojo.User;

/**
 * Description:
 * Ahthor:Jin Zhengsen
 * Date:2021/1/31  23:18
 */
public interface UserDao {
    User findUserByUsername(String username);
}
