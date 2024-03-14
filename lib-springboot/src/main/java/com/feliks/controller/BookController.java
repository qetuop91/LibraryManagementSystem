package com.feliks.controller;

import com.feliks.controller.request.BookPageRequest;
import com.feliks.controller.request.UploadRequest;
import com.feliks.entity.Book;
import com.feliks.entity.ResponseResult;
import com.feliks.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@CrossOrigin
@RequestMapping("/book")
public class BookController {

    @Autowired
    private BookService bookService;

    // 查询所有用户
    @GetMapping("/list")
    public ResponseResult listBooks() {
        return bookService.listBooks();
    }

    // 分类生成树
    @GetMapping("/tree")
    public ResponseResult tree() {
        return bookService.tree();
    }

    // 模糊查询
    @GetMapping("/page")
    public ResponseResult page(BookPageRequest bookPageRequest) {
        return bookService.listByCondition(bookPageRequest);
    }

    // 添加书籍
    @PostMapping("/save")
    public ResponseResult addBook(@RequestBody Book book) {
        return bookService.addBook(book);
    }

    // 编辑书籍信息
    // ① 根据id获取数据回显到前端
    @GetMapping("/{id}")
    public ResponseResult getBookById(@PathVariable("id") Integer id) {
        return bookService.getBookById(id);
    }

    // ② 获取修改后的书籍信息并根据id修改数据库中对应的书籍数据
    @PutMapping("/update")
    public ResponseResult updateBookById(@RequestBody Book book) {
        return bookService.updateBookById(book);
    }

    // 删除书籍
    @DeleteMapping("/delete/{id}")
    public ResponseResult deleteBookById(@PathVariable("id") Integer id) {
        return bookService.deleteBookById(id);
    }

    // 上传书籍封面
    @PostMapping("/upload")
    public ResponseResult uploadFile(MultipartFile file) {
        return bookService.uploadFile(file);
    }

    @GetMapping("/download/{isFlag}")
    public ResponseResult downloadFile(@PathVariable String isFlag, @RequestParam(required = false) String play, HttpServletResponse response) {
        return bookService.downloadFile(isFlag, play, response);
    }
}
