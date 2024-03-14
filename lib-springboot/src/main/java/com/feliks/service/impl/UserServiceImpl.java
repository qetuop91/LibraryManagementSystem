package com.feliks.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.feliks.annotation.mySystemlog;
import com.feliks.controller.request.UserPageRequest;
import com.feliks.entity.ResponseResult;
import com.feliks.entity.User;
import com.feliks.mapper.UserMapper;
import com.feliks.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    // 查询user表所有结果
    @Override
    @mySystemlog(xxbusinessName = "查询所有User")//接口描述，用于'日志记录'功能
    public ResponseResult listUsers() {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        List<User> users = list(queryWrapper);
        return ResponseResult.okResult(users);
    }

    // 根据姓名和手机号模糊查询user表中的记录并分页
    @Override
    @mySystemlog(xxbusinessName = "根据条件查询User")//接口描述，用于'日志记录'功能
    public ResponseResult listByCondition(UserPageRequest userPageRequest) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.hasText(userPageRequest.getName()), User::getName, userPageRequest.getName());
        queryWrapper.like(StringUtils.hasText(userPageRequest.getPhone()), User::getPhone, userPageRequest.getPhone());
        queryWrapper.orderByDesc(User::getId);
        Page<User> userPage = new Page<>(userPageRequest.getPageNum(), userPageRequest.getPageSize());
        page(userPage, queryWrapper);
        return ResponseResult.okResult(userPage);
    }

    @Override
    @mySystemlog(xxbusinessName = "添加User")//接口描述，用于'日志记录'功能
    public ResponseResult addUser(User user) {
        Date date = new Date();
        // 把用户名当作卡号来处理
        user.setUsername(DateUtil.format(date, "yyyyMMdd") + Math.abs(IdUtil.fastSimpleUUID().hashCode()));
        save(user);
        return ResponseResult.okResult();
    }

    @Override
    @mySystemlog(xxbusinessName = "根据id获取User")//接口描述，用于'日志记录'功能
    public ResponseResult getUserById(Integer id) {
        User user = getById(id);
        return ResponseResult.okResult(user);
    }

    @Override
    @mySystemlog(xxbusinessName = "根据id更新User")//接口描述，用于'日志记录'功能
    public ResponseResult updateUserById(User user) {
        updateById(user);
        return ResponseResult.okResult();
    }

    @Override
    @mySystemlog(xxbusinessName = "根据id删除User")//接口描述，用于'日志记录'功能
    public ResponseResult deleteUserById(Integer id) {
        removeById(id);
        return ResponseResult.okResult();
    }

    @Override
    @mySystemlog(xxbusinessName = "设置User的账户余额")//接口描述，用于'日志记录'功能
    public ResponseResult handleAccount(User user) {
        Integer score = user.getScore();
        if (score == null) {
            return ResponseResult.okResult();
        }
        Integer id = user.getId();
        User dbUser = getById(id);
        dbUser.setAccount(dbUser.getAccount() + score);
        updateById(dbUser);
        return ResponseResult.okResult();
    }

}
