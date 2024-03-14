package com.feliks.controller;

import com.feliks.controller.request.AdminPageRequest;
import com.feliks.entity.Admin;
import com.feliks.controller.request.PasswordRequest;
import com.feliks.entity.ResponseResult;
import com.feliks.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@CrossOrigin
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    AdminService adminService;

    // 查询所有用户
    @GetMapping("/list")
    public ResponseResult listAdmins() {
        return adminService.listAdmins();
    }

    // 模糊查询
    @GetMapping("/page")
    public ResponseResult page(AdminPageRequest adminPageRequest) {
        return adminService.listByCondition(adminPageRequest);
    }

    // 添加用户
    @PostMapping("/save")
    public ResponseResult addAdmin(@RequestBody Admin admin) {
        return adminService.addAdmin(admin);
    }

    // 编辑用户
    // ① 根据id获取数据回显到前端
    @GetMapping("/{id}")
    public ResponseResult getAdminById(@PathVariable("id") Integer id) {
        return adminService.getAdminById(id);
    }

    // ② 获取修改后的用户信息并根据id修改数据库中对应的用户数据
    @PutMapping("/update")
    public ResponseResult updateAdminById(@RequestBody Admin admin) {
        return adminService.updateAdminById(admin);
    }

    // 删除用户
    @DeleteMapping("/delete/{id}")
    public ResponseResult deleteUserById(@PathVariable("id") Integer id) {
        return adminService.deleteAdminById(id);
    }

    // 修改密码
    @PutMapping("/changePassword")
    public ResponseResult changePassword(@RequestBody PasswordRequest passwordRequest) {
        return adminService.changePassword(passwordRequest);
    }
}
