package com.jzs.dao;

import com.jzs.pojo.Permission;

import java.util.Set;

/**
 * Description:
 * Ahthor:Jin Zhengsen
 * Date:2021/1/31  23:20
 */
public interface PermissionDao {
    Set<Permission> findPermissionsByRoleId(Integer roleId);
}
