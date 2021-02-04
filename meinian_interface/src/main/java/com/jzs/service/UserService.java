package com.jzs.service;

import com.jzs.pojo.User;

/**
 * Description:
 * Ahthor:Jin Zhengsen
 * Date:2021/1/31  23:01
 */
public interface UserService {
    User findUserByUsername(String username);
}
