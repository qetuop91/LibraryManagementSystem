package com.feliks.controller;

import com.feliks.controller.request.UserPageRequest;
import com.feliks.entity.ResponseResult;
import com.feliks.entity.User;
import com.feliks.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@CrossOrigin
@RequestMapping("/user")
public class UserController {
    @Autowired
    UserService userService;

    // 查询所有用户
    @GetMapping("/list")
    public ResponseResult listUsers() {
        return userService.listUsers();
    }

    // 模糊查询
    @GetMapping("/page")
    public ResponseResult page(UserPageRequest userPageRequest) {
        return userService.listByCondition(userPageRequest);
    }

    // 添加用户
    @PostMapping("/save")
    public ResponseResult addUser(@RequestBody User user) {
        return userService.addUser(user);
    }

    // 编辑用户
    // ① 根据id获取数据回显到前端
    @GetMapping("/{id}")
    public ResponseResult getUserById(@PathVariable("id") Integer id) {
        return userService.getUserById(id);
    }

    // ② 获取修改后的用户信息并根据id修改数据库中对应的用户数据
    @PutMapping("/update")
    public ResponseResult updateUserById(@RequestBody User user) {
        return userService.updateUserById(user);
    }

    // 删除用户
    @DeleteMapping("/delete/{id}")
    public ResponseResult deleteUserById(@PathVariable("id") Integer id) {
        return userService.deleteUserById(id);
    }

    //充值接口
    @PostMapping("/account")
    public ResponseResult addAccount(@RequestBody User user) {
        return userService.handleAccount(user);
    }
}
