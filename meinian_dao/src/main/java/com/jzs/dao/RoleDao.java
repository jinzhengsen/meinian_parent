package com.jzs.dao;

import com.jzs.pojo.Role;

import java.util.Set;

/**
 * Description:
 * Ahthor:Jin Zhengsen
 * Date:2021/1/31  23:19
 */
public interface RoleDao {
    Set<Role> findRolesByUserId(Integer userId);
}
