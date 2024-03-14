package com.feliks.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;
/**
 * (Admin)表实体类
 *
 * @author makejava
 * @since 2024-03-02 17:51:09
 */
@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("admin")
public class Admin extends BaseEntity implements Serializable  {
    @TableId(type = IdType.AUTO)
    private Integer id;
    //用户名
    private String username;
    //密码
    private String password;
    //联系方式
    private String phone;
    //邮箱
    private String email;
//    //创建时间
//    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss", timezone = "GMT+8")
//    private Date createTime;
//    //更新时间
//    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss", timezone = "GMT+8")
//    private Date updateTime;
    //状态（1：正常 0：禁用）
    private boolean status;

    public boolean getStatus() {
        return status;
    }

}
