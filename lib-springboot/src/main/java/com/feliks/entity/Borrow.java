package com.feliks.entity;

import java.util.Date;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
/**
 * (Borrow)表实体类
 *
 * @author makejava
 * @since 2024-03-09 15:44:28
 */
@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("borrow")
public class Borrow extends BaseEntity {

    //id
    @TableId(type = IdType.AUTO)
    private Integer id;

    //书籍名称    
    private String bookName;
    //书籍标准码    
    private String bookNumber;
    //用户id    
    private Integer userId;
    //用户名    
    private String userName;
    //用户卡号
    private String userUsername;
    //用户联系方式
    private String userPhone;
    //用户借书积分    
    private Integer score;
    //书籍剩余数量
    @TableField(exist = false)
    private Integer nums;
    //用户剩余积分
    @TableField(exist = false)
    private Integer account;
    //用户借书状态
    private String status;
    //用户借书天数
    private Integer days;
    //书籍归还日期
    private Date returnDate;

    @Override
    public String toString() {
        return "Borrow{" +
                "id=" + id +
                ", bookName='" + bookName + '\'' +
                ", bookNumber='" + bookNumber + '\'' +
                ", userId=" + userId +
                ", userName='" + userName + '\'' +
                ", userUsername='" + userUsername + '\'' +
                ", userPhone='" + userPhone + '\'' +
                ", score=" + score +
                '}';
    }
}
