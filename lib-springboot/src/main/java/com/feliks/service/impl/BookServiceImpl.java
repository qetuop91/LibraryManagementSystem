package com.feliks.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.feliks.annotation.mySystemlog;
import com.feliks.controller.request.BookPageRequest;
import com.feliks.controller.request.UploadRequest;
import com.feliks.entity.Book;
import com.feliks.entity.Category;
import com.feliks.entity.LoginAdmin;
import com.feliks.entity.ResponseResult;
import com.feliks.enums.AppHttpCodeEnum;
import com.feliks.mapper.BookMapper;
import com.feliks.service.BookService;
import com.feliks.service.CategoryService;
import com.feliks.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * (Book)表服务实现类
 *
 * @author makejava
 * @since 2024-03-08 17:55:45
 */
@Service
public class BookServiceImpl extends ServiceImpl<BookMapper, Book> implements BookService {

    private static final String BASE_FILE_PATH = System.getProperty("user.dir") + "/files/";

    @Override
    @mySystemlog(xxbusinessName = "查询所有Book")//接口描述，用于'日志记录'功能
    public ResponseResult listBooks() {
        LambdaQueryWrapper<Book> queryWrapper = new LambdaQueryWrapper<>();
        List<Book> books = list(queryWrapper);
        return ResponseResult.okResult(books);
    }

    @Autowired
    private CategoryService categoryService;

    @Override
    @mySystemlog(xxbusinessName = "查询分类树")//接口描述，用于'日志记录'功能
    public ResponseResult tree() {
        List<Category> categories = (List<Category>) categoryService.listCategorys().getData();
        List<Category> tree = createTree(null, categories);
        return ResponseResult.okResult(tree);
    }


    @Override
    public ResponseResult uploadFile(MultipartFile file) {
        String originalFilename = file.getOriginalFilename();
        long isFlag = System.currentTimeMillis();
        String filePath = BASE_FILE_PATH + isFlag + "_" + originalFilename;

        try {
            FileUtil.mkParentDirs(filePath);
            file.transferTo(FileUtil.file(filePath));
            String localURL = "http://localhost:8080/api/book/download/" + isFlag;
            String tomcatURL = "http://10.23.127.99:8080/LibraryManagement/api/book/download/" + isFlag;
            return ResponseResult.okResult(tomcatURL);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ResponseResult.errorResult(AppHttpCodeEnum.FILE_UPLOAD_ERROR);
    }

    @Override
    public ResponseResult downloadFile(String isFlag, String play, HttpServletResponse response) {
        OutputStream outputStream;
        List<String> fileNames = FileUtil.listFileNames(BASE_FILE_PATH);
        String fileName = fileNames.stream().filter(name -> name.contains(isFlag)).findAny().orElse("");
        try {
            if (StrUtil.isNotEmpty(fileName)) {
                String realName = fileName.substring(fileName.indexOf("_") + 1);
                if ("1".equals(play)) {
                    //预览下载
                    response.addHeader("Content-Disposition", "inline;filename=" + URLEncoder.encode(realName, "UTF-8"));
                } else {
                    //附件下载
                    response.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(realName, "UTF-8"));
                }
                byte[] bytes = FileUtil.readBytes(BASE_FILE_PATH + fileName);
                outputStream = response.getOutputStream();
                outputStream.write(bytes);
                outputStream.flush();
                outputStream.close();
            }
            return ResponseResult.okResult();
        } catch (Exception e) {
            return ResponseResult.errorResult(AppHttpCodeEnum.FILE_DOWNLOAD_ERROR);
        }
    }

    // 递归生成分类树
    private List<Category> createTree(Integer pid, List<Category> categories) {
        List<Category> treeCategory = new ArrayList<>();
        for (Category category : categories) {
            //判断如果传进来的pid和上一次传进来的category列表里的pid都是null
            //则认为当前的category对象为主分类，那他的children就应该为其递归id生成的所有分类
            if (pid == null) {
                if (category.getPid() == null) {
                    treeCategory.add(category);
                    category.setChildren(createTree(category.getId(), categories));
                }
            } else {
                if (pid.equals(category.getPid())) {
                    treeCategory.add(category);
                    category.setChildren(createTree(category.getId(), categories));
                }
            }
            if (CollUtil.isEmpty(category.getChildren())) {
                category.setChildren(null);
            }

        }
        return treeCategory;
    }

    @Override
    @mySystemlog(xxbusinessName = "根据条件查询Book")//接口描述，用于'日志记录'功能
    public ResponseResult listByCondition(BookPageRequest bookPageRequest) {
        LambdaQueryWrapper<Book> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.hasText(bookPageRequest.getBookName()), Book::getBookName, bookPageRequest.getBookName());
        queryWrapper.like(StringUtils.hasText(bookPageRequest.getBookNumber()), Book::getBookNumber, bookPageRequest.getBookNumber());
        queryWrapper.orderByDesc(Book::getId);
        Page<Book> bookPage = new Page<>(bookPageRequest.getPageNum(), bookPageRequest.getPageSize());
        page(bookPage, queryWrapper);
        return ResponseResult.okResult(bookPage);
    }

    @Override
    @mySystemlog(xxbusinessName = "添加Book")//接口描述，用于'日志记录'功能
    public ResponseResult addBook(Book book) {
        //获取从前端传过来的分类路径数组
        List<String> categories = book.getCategories();
        book.setCategory(category(categories));
        //校验数据库中有没有重复的标准码
        LambdaQueryWrapper<Book> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Book::getBookNumber, book.getBookNumber());
        if (list(queryWrapper).size() != 0) {
            return ResponseResult.errorResult(AppHttpCodeEnum.BOOKNUMBER_EXIST);
        }
        save(book);
        return ResponseResult.okResult();
    }

    @Override
    @mySystemlog(xxbusinessName = "根据id获取Book")//接口描述，用于'日志记录'功能
    public ResponseResult getBookById(Integer id) {
        Book book = getById(id);
        return ResponseResult.okResult(book);
    }

    @Override
    @mySystemlog(xxbusinessName = "根据id更新Book")//接口描述，用于'日志记录'功能
    public ResponseResult updateBookById(Book book) {
        //获取分类数组
        List<String> categories = book.getCategories();
        book.setCategory(category(categories));
        updateById(book);
        return ResponseResult.okResult();
    }

    //将前端接收回来的分类数组转换成xxx > xxx > xxx格式的分类路径
    private String category(List<String> categories) {
        StringBuilder stringBuilder = new StringBuilder();
        if (CollUtil.isNotEmpty(categories)) {
            categories.forEach(v -> stringBuilder.append(v).append(" > "));
            return stringBuilder.substring(0, stringBuilder.lastIndexOf(" > "));
        }
        return stringBuilder.toString();
    }

    @Override
    @mySystemlog(xxbusinessName = "根据id删除Book")//接口描述，用于'日志记录'功能
    public ResponseResult deleteBookById(Integer id) {
        removeById(id);
        return ResponseResult.okResult();
    }

}
