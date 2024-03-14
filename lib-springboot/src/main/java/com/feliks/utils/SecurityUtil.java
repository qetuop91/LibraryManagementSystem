package com.feliks.utils;

import com.feliks.entity.LoginAdmin;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtil {

    //获取管理员
    public static LoginAdmin getLoginAdmin() {
        return (LoginAdmin) getAuthentication().getPrincipal();
    }

    //获取Authentication
    public static Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    //指定userId为1的用户就是超级管理员
    public static Boolean isAdmin() {
        Integer id = getAdminId();
        return id != null && id == 1;
    }

    //获取userId
    public static Integer getAdminId() {
        return getLoginAdmin().getAdmin().getId();
    }
}
