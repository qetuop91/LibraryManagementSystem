package com.feliks.controller;

import com.feliks.controller.request.CategoryPageRequest;
import com.feliks.entity.Category;
import com.feliks.entity.ResponseResult;
import com.feliks.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    // 查询所有分类
    @GetMapping("/list")
    public ResponseResult listCategorys() {
        return categoryService.listCategorys();
    }

    // 模糊查询
    @GetMapping("/page")
    public ResponseResult page(CategoryPageRequest categoryPageRequest) {
        return categoryService.listByCondition(categoryPageRequest);
    }

    // 添加分类
    @PostMapping("/save")
    public ResponseResult addCategory(@RequestBody Category category) {
        return categoryService.addCategory(category);
    }

    // 编辑分类
    // ① 根据id获取数据回显到前端
    @GetMapping("/{id}")
    public ResponseResult getCategoryById(@PathVariable("id") Integer id) {
        return categoryService.getCategoryById(id);
    }

    // ② 获取修改后的分类信息并根据id修改数据库中对应的分类数据
    @PutMapping("/update")
    public ResponseResult updateCategoryById(@RequestBody Category category) {
        return categoryService.updateCategoryById(category);
    }

    // 删除分类
    @DeleteMapping("/delete/{id}")
    public ResponseResult deleteCategoryById(@PathVariable("id") Integer id) {
        return categoryService.deleteCategoryById(id);
    }
}
