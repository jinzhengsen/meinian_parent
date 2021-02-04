package com.jzs.security;

import com.alibaba.dubbo.config.annotation.Reference;
import com.jzs.pojo.Permission;
import com.jzs.pojo.Role;
import com.jzs.pojo.User;
import com.jzs.service.UserService;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Description:
 * Ahthor:Jin Zhengsen
 * Date:2021/1/31  22:46
 */
@Component
public class SpringSecurityUserService implements UserDetailsService {
    @Reference
    UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //远程调用用户服务，根据用户名查询用户信息
        User user=userService.findUserByUsername(username);
        if (user==null){
            //用户名不存在，抛出异常UsernameNotFoundException
            return null;
        }
        List<GrantedAuthority> list=new ArrayList<GrantedAuthority>();
        Set<Role> roles=user.getRoles();
        for (Role role : roles) {
            Set<Permission> permissions=role.getPermissions();
            for (Permission permission : permissions) {
                //授权
                list.add(new SimpleGrantedAuthority(permission.getKeyword()));
            }
        }
        /**
         * User()
         * 1：指定用户名
         * 2：指定密码（SpringSecurity会自动对密码进行校验）
         * 3：传递授予的角色和权限
         */
        UserDetails userDetails=new org.springframework.security.core.userdetails.User(username,user.getPassword(),list);
        return userDetails;
    }
}
