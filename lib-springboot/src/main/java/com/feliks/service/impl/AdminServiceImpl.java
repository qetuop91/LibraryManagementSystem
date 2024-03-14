package com.feliks.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.feliks.annotation.mySystemlog;
import com.feliks.controller.request.AdminPageRequest;
import com.feliks.controller.request.PasswordRequest;
import com.feliks.entity.Admin;
import com.feliks.entity.ResponseResult;
import com.feliks.enums.AppHttpCodeEnum;
import com.feliks.mapper.AdminMapper;
import com.feliks.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;

@Service
public class AdminServiceImpl extends ServiceImpl<AdminMapper, Admin> implements AdminService {

    @Autowired
    PasswordEncoder passwordEncoder;

    private static final String DEFAULT_PASSWORD = "123456";

    private String encode;

    // 查询所有admin
    @Override
    @mySystemlog(xxbusinessName = "查询所有Admin")//接口描述，用于'日志记录'功能
    public ResponseResult listAdmins() {
        LambdaQueryWrapper<Admin> queryWrapper = new LambdaQueryWrapper<>();
        List<Admin> admins = list(queryWrapper);
        return ResponseResult.okResult(admins);
    }

    // 根据username、phone、email模糊查询
    @Override
    @mySystemlog(xxbusinessName = "根据条件查询Admin")//接口描述，用于'日志记录'功能
    public ResponseResult listByCondition(AdminPageRequest adminPageRequest) {
        LambdaQueryWrapper<Admin> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.hasText(adminPageRequest.getUsername()), Admin::getUsername, adminPageRequest.getUsername());
        queryWrapper.like(StringUtils.hasText(adminPageRequest.getPhone()), Admin::getPhone, adminPageRequest.getPhone());
        queryWrapper.like(StringUtils.hasText(adminPageRequest.getEmail()), Admin::getEmail, adminPageRequest.getEmail());
        queryWrapper.orderByDesc(Admin::getId);
        Page<Admin> adminPage = new Page<>(adminPageRequest.getPageNum(), adminPageRequest.getPageSize());
        page(adminPage, queryWrapper);
        return ResponseResult.okResult(adminPage);
    }

    @Autowired
    private AdminMapper adminMapper;

    @Override
    @mySystemlog(xxbusinessName = "添加Admin")//接口描述，用于'日志记录'功能
    public ResponseResult addAdmin(Admin admin) {
        LambdaQueryWrapper<Admin> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Admin::getUsername, admin.getUsername());
        Admin selectOne = adminMapper.selectOne(queryWrapper);
        if (selectOne != null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.USERNAME_EXIST);
        }

        encode = passwordEncoder.encode(DEFAULT_PASSWORD);
        System.out.println("encode: " + encode);
        if (admin.getPassword() == null) {
            admin.setPassword(encode);
        }
        System.out.println("admin.getPassword(): " + admin.getPassword());
        save(admin);
        return ResponseResult.okResult();
    }

    @Override
    @mySystemlog(xxbusinessName = "根据id获取Admin")//接口描述，用于'日志记录'功能
    public ResponseResult getAdminById(Integer id) {
        Admin admin = getById(id);
        return ResponseResult.okResult(admin);
    }

    @Override
    @mySystemlog(xxbusinessName = "根据id更新Admin")//接口描述，用于'日志记录'功能
    public ResponseResult updateAdminById(Admin admin) {
        updateById(admin);
        return ResponseResult.okResult();
    }

    @Override
    @mySystemlog(xxbusinessName = "根据id删除Admin")//接口描述，用于'日志记录'功能
    public ResponseResult deleteAdminById(Integer id) {
        if(id == 1) {
            return ResponseResult.errorResult(AppHttpCodeEnum.IS_ADMIN);
        }
        removeById(id);
        return ResponseResult.okResult();
    }

    @Override
    @mySystemlog(xxbusinessName = "更改Admin的密码")//接口描述，用于'日志记录'功能
    public ResponseResult changePassword(PasswordRequest passwordRequest) {
        System.out.println("change password");
        Admin admin = getById(passwordRequest.getId());
        encode = passwordEncoder.encode(passwordRequest.getNewPassword());
        System.out.println("userId: "
                + admin.getId()
                + "\npassword: "
                + passwordRequest.getPassword());

        System.out.println("encode: " + encode);
        admin.setPassword(encode);
        updateById(admin);

        System.out.println("userId: "
                + admin.getId()
                + "\npassword: "
                + admin.getPassword());
        return ResponseResult.okResult();
    }

}
