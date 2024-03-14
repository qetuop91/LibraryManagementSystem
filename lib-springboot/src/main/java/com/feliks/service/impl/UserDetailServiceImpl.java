package com.feliks.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.feliks.annotation.mySystemlog;
import com.feliks.entity.Admin;
import com.feliks.entity.LoginAdmin;
import com.feliks.mapper.AdminMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class UserDetailServiceImpl implements UserDetailsService {

    @Autowired
    private AdminMapper adminMapper;

    @Override
    @mySystemlog(xxbusinessName = "loadUserByUsername")//接口描述，用于'日志记录'功能
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        LambdaQueryWrapper<Admin> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Admin::getUsername, username);
        Admin admin = adminMapper.selectOne(queryWrapper);
        if (Objects.isNull(admin)) {
            throw new RuntimeException("用户不存在");
        }
        return new LoginAdmin(admin);
    }
}
