package com.feliks.entity;

import java.util.Date;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
/**
 * (Returnbook)表实体类
 *
 * @author makejava
 * @since 2024-03-12 20:24:24
 */
@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("returnbook")
public class Returnbook extends BaseEntity  {

    @TableId(type = IdType.AUTO)
    //归还时间表的id    
    private Integer id;

    
}
