package com.feliks.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.feliks.controller.request.BookPageRequest;
import com.feliks.controller.request.UploadRequest;
import com.feliks.entity.Book;
import com.feliks.entity.ResponseResult;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


/**
 * (Book)表服务接口
 *
 * @author makejava
 * @since 2024-03-08 17:55:45
 */
public interface BookService extends IService<Book> {
    ResponseResult listBooks();

    ResponseResult listByCondition(BookPageRequest bookPageRequest);

    ResponseResult addBook(Book book);

    ResponseResult getBookById(Integer id);

    ResponseResult updateBookById(Book book);

    ResponseResult deleteBookById(Integer id);

    ResponseResult tree();

    ResponseResult uploadFile(MultipartFile file);

    ResponseResult downloadFile(String isFlag, String play, HttpServletResponse response);
}
