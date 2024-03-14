package com.feliks.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("user")
public class User extends BaseEntity {
    @TableId(type = IdType.AUTO)
    private Integer id;
    //用户名
    private String name;
    //卡号
    private String username;
    //年龄
    private Integer age;
    //性别
    private String sex;
    //联系方式
    private String phone;
    //地址
    private String address;
    //账户积分余额
    private Integer account;
    //积分
    @TableField(exist = false)
    private Integer score;
    //当前用户一共借用的书籍数
    private Integer borrowBook;
}
