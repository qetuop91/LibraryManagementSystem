package com.feliks.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.feliks.controller.request.AdminPageRequest;
import com.feliks.controller.request.PasswordRequest;
import com.feliks.entity.Admin;
import com.feliks.entity.ResponseResult;

public interface AdminService extends IService<Admin> {
    ResponseResult listAdmins();

    ResponseResult listByCondition(AdminPageRequest adminPageRequest);

    ResponseResult addAdmin(Admin admin);

    ResponseResult getAdminById(Integer id);

    ResponseResult updateAdminById(Admin admin);

    ResponseResult deleteAdminById(Integer id);

    ResponseResult changePassword(PasswordRequest passwordRequest);
}
