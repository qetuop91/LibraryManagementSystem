package com.feliks.entity;

import java.util.Date;

import java.io.Serializable;
import java.util.List;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
/**
 * (Book)表实体类
 *
 * @author makejava
 * @since 2024-03-08 17:55:43
 */
@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("book")
public class Book extends BaseEntity  {
    //id
    @TableId(type = IdType.AUTO)
    private Integer id;
    //书名
    private String bookName;
    //描述
    private String description;
    //出版日期
    private String publishDate;
    //作者
    private String author;
    //出版社
    private String publish;
    //分类
    private String category;
    //书本国际编码
    private String bookNumber;
    //封面图片
    private String cover;
    //书籍积分
    private Integer score;
    //书籍数量
    private Integer nums;
    //新增图书时选择的分类数组
    //由于数据库中没有children字段，所以要添加这个注解让mybatis-plus不查询这个字段
    @TableField(exist = false)
    private List<String> categories;
}
