package com.feliks.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.feliks.annotation.mySystemlog;
import com.feliks.entity.Admin;
import com.feliks.entity.LoginAdmin;
import com.feliks.entity.ResponseResult;
import com.feliks.enums.AppHttpCodeEnum;
import com.feliks.mapper.AdminMapper;
import com.feliks.service.LoginService;
import com.feliks.utils.JwtUtil;
import com.feliks.utils.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Objects;

@Service
public class LoginServiceImpl extends ServiceImpl<AdminMapper, Admin> implements LoginService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private RedisCache redisCache;

    // 登录
    @Override
    @mySystemlog(xxbusinessName = "登录")//接口描述，用于'日志记录'功能
    public ResponseResult loginAdmin(Admin admin) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(admin.getUsername(), admin.getPassword());
        Authentication authentication = authenticationManager.authenticate(authenticationToken);
        if (Objects.isNull(authentication)) {
            throw new RuntimeException("用户名或密码错误");
        }
        LoginAdmin loginAdmin = (LoginAdmin) authentication.getPrincipal();
        if (!loginAdmin.getAdmin().getStatus()) {
            throw new RuntimeException("当前用户被禁用，请联系管理员");
        }
        String userId = loginAdmin.getAdmin().getId().toString();
        String jwt = JwtUtil.createJWT(userId);
        System.out.println("jwt: " + jwt);
        redisCache.setCacheObject("Login:" + userId, loginAdmin);

        String id = loginAdmin.getAdmin().getId().toString();
        String username = loginAdmin.getAdmin().getUsername();
        String phone = loginAdmin.getAdmin().getPhone();
        String email = loginAdmin.getAdmin().getEmail();


        // 封装token返回
        HashMap<String, String> map = new HashMap<>();
        map.put("token", jwt);
        map.put("id", id);
        map.put("username", username);
        map.put("phone", phone);
        map.put("email", email);
        return new ResponseResult(200, "登陆成功", map);
    }

    // 退出登录
    @Override
    @mySystemlog(xxbusinessName = "退出登录")//接口描述，用于'日志记录'功能
    public ResponseResult logoutAdmin() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LoginAdmin loginAdmin = (LoginAdmin) authentication.getPrincipal();
        String userId = loginAdmin.getAdmin().getId().toString();

        redisCache.deleteObject("Login:" + userId);
        return new ResponseResult(200, "退出成功");
    }

    @Override
    @mySystemlog(xxbusinessName = "检查登陆的Admin")//接口描述，用于'日志记录'功能
    public ResponseResult checkAdmin(Integer id) {
        LambdaQueryWrapper<Admin> adminLambdaQueryWrapper = new LambdaQueryWrapper<>();
        adminLambdaQueryWrapper.eq(Admin::getId, id);
        Admin admin = getOne(adminLambdaQueryWrapper);
        if (!admin.getStatus()) {
            return ResponseResult.errorResult(AppHttpCodeEnum.NO_OPERATOR_AUTH);
        }
        if (admin == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.ADMIN_IS_NOT_EXIST);
        }
        return ResponseResult.okResult();
    }
}
