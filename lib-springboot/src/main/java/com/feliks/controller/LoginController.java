package com.feliks.controller;

import com.feliks.entity.Admin;
import com.feliks.entity.ResponseResult;
import com.feliks.enums.AppHttpCodeEnum;
import com.feliks.exception.SystemException;
import com.feliks.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

@RestController
public class LoginController {

    @Autowired
    private LoginService loginService;

    @PostMapping("/login")
    public ResponseResult Login(@RequestBody Admin admin) {
        if(!StringUtils.hasText(admin.getUsername())) {
            //提示必须要输入用户名
            throw new SystemException(AppHttpCodeEnum.REQUIRE_USERNAME);
        }
        return loginService.loginAdmin(admin);
    }

    @GetMapping("/check/{id}")
    public ResponseResult Check(@PathVariable Integer id) {
        return loginService.checkAdmin(id);
    }

    @PostMapping("/logout")
    public ResponseResult Logout() {
        return loginService.logoutAdmin();
    }

}
