package com.feliks.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.feliks.controller.request.CategoryPageRequest;
import com.feliks.entity.Category;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CategoryMapper extends BaseMapper<Category> {
    List<Category> listByCondition(CategoryPageRequest categoryPageRequest);
}
