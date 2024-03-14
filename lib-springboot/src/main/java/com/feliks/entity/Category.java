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
import java.util.List;

/**
 * (Category)表实体类
 *
 * @author makejava
 * @since 2024-03-07 21:21:07
 */
@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("category")
public class Category extends BaseEntity  {
    // 分类id
    @TableId(type = IdType.AUTO)
    private Integer id;
    //名称
    private String categoryName;
    //备注
    private String remark;
    //父级id
    private Integer pid;
    //由于数据库中没有children字段，所以要添加这个注解让mybatis-plus不查询这个字段
    @TableField(exist = false)
    private List<Category> children;
    
}
