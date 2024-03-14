package com.feliks.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.feliks.annotation.mySystemlog;
import com.feliks.controller.request.CategoryPageRequest;
import com.feliks.entity.Category;
import com.feliks.entity.ResponseResult;
import com.feliks.enums.AppHttpCodeEnum;
import com.feliks.mapper.CategoryMapper;
import com.feliks.service.CategoryService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {
    @Override
    @mySystemlog(xxbusinessName = "查询所有Category")//接口描述，用于'日志记录'功能
    public ResponseResult listCategorys() {
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        List<Category> categories = list(queryWrapper);
        return ResponseResult.okResult(categories);
    }

    @Override
    @mySystemlog(xxbusinessName = "根据条件查询Category")//接口描述，用于'日志记录'功能
    public ResponseResult listByCondition(CategoryPageRequest categoryPageRequest) {
        PageHelper.startPage(categoryPageRequest.getPageNum(), categoryPageRequest.getPageSize());
        List<Category> categories = categoryMapper.listByCondition(categoryPageRequest);
        PageInfo<Category> categoryPageInfo = new PageInfo<>(categories);
        return ResponseResult.okResult(categoryPageInfo);
    }

    @Autowired
    private CategoryMapper categoryMapper;

    @Override
    @mySystemlog(xxbusinessName = "添加Category")//接口描述，用于'日志记录'功能
    public ResponseResult addCategory(Category category) {
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Category::getCategoryName, category.getCategoryName());
        Category selectOne = categoryMapper.selectOne(queryWrapper);
        if (selectOne != null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.CATEGORYNAME_EXIST);
        }
        if(category.getPid() != null) {
            save(category);

        }else {
            category.setPid(null);
            save(category);
        }
        return ResponseResult.okResult();
    }

    @Override
    @mySystemlog(xxbusinessName = "根据id查询Category")//接口描述，用于'日志记录'功能
    public ResponseResult getCategoryById(Integer id) {
        Category category = getById(id);
        return ResponseResult.okResult(category);
    }

    @Override
    @mySystemlog(xxbusinessName = "根据id更新Category")//接口描述，用于'日志记录'功能
    public ResponseResult updateCategoryById(Category category) {
        updateById(category);
        return ResponseResult.okResult();
    }

    @Override
    @mySystemlog(xxbusinessName = "根据id删除Category")//接口描述，用于'日志记录'功能
    public ResponseResult deleteCategoryById(Integer id) {
        removeById(id);
        return ResponseResult.okResult();
    }
}
