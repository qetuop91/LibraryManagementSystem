package com.feliks.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.feliks.entity.Admin;
import com.feliks.entity.Book;
import com.feliks.entity.ResponseResult;

public interface LoginService extends IService<Admin> {

    ResponseResult loginAdmin(Admin admin);

    ResponseResult logoutAdmin();

    ResponseResult checkAdmin(Integer id);
}
