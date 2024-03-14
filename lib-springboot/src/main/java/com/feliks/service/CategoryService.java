package com.feliks.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.feliks.controller.request.CategoryPageRequest;
import com.feliks.entity.Category;
import com.feliks.entity.ResponseResult;

public interface CategoryService extends IService<Category> {
    ResponseResult listCategorys();

    ResponseResult listByCondition(CategoryPageRequest categoryPageRequest);

    ResponseResult addCategory(Category category);

    ResponseResult getCategoryById(Integer id);

    ResponseResult updateCategoryById(Category category);

    ResponseResult deleteCategoryById(Integer id);
}
