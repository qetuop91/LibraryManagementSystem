package com.feliks.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.feliks.entity.Book;
import org.apache.ibatis.annotations.Mapper;


/**
 * (Book)表数据库访问层
 *
 * @author makejava
 * @since 2024-03-08 17:55:43
 */
@Mapper
public interface BookMapper extends BaseMapper<Book> {

}
