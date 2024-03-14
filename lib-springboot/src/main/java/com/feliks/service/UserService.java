package com.feliks.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.feliks.controller.request.UserPageRequest;
import com.feliks.entity.ResponseResult;
import com.feliks.entity.User;

public interface UserService extends IService<User> {
    ResponseResult listUsers();

    ResponseResult listByCondition(UserPageRequest userPageRequest);

    ResponseResult addUser(User user);

    ResponseResult getUserById(Integer id);

    ResponseResult updateUserById(User user);

    ResponseResult deleteUserById(Integer id);

    ResponseResult handleAccount(User user);
}
