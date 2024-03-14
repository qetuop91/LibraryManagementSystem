package com.feliks.controller;

import com.feliks.controller.request.BorrowPageRequest;
import com.feliks.dto.BorrowDto;
import com.feliks.entity.Borrow;
import com.feliks.entity.ResponseResult;
import com.feliks.service.BorrowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/borrow")
public class BorrowController {

    @Autowired
    private BorrowService borrowService;

    // 查询所有
    @GetMapping("/list")
    public ResponseResult listBorrows() {
        return borrowService.listBorrows();
    }

    // 模糊查询
    @GetMapping("/page")
    public ResponseResult page(BorrowPageRequest borrowPageRequest) {
        return borrowService.listByCondition(borrowPageRequest);
    }

    // 添加借阅信息
    @PostMapping("/save")
    public ResponseResult addBorrow(@RequestBody Borrow borrow) {
        return borrowService.addBorrow(borrow);
    }

    //还书
    @PostMapping("/returnBook/{id}")
    public ResponseResult returnBook(@PathVariable("id") Integer id) {
        return borrowService.returnBook(id);
    }

    // 编辑借阅信息
    // ① 根据id获取数据回显到前端
    @GetMapping("/{id}")
    public ResponseResult getBorrowById(@PathVariable("id") Integer id) {
        return borrowService.getBorrowById(id);
    }

    // ② 获取修改后的借阅信息并根据id修改数据库中对应的借阅数据
    @PutMapping("/update")
    public ResponseResult updateBorrowById(@RequestBody Borrow borrow) {
        return borrowService.updateBorrowById(borrow);
    }

    // 删除借阅信息
    @DeleteMapping("/delete/{id}")
    public ResponseResult deleteBorrowById(@PathVariable("id") Integer id) {
        return borrowService.deleteBorrowById(id);
    }

    //首页借阅情况图表数据接口
    @GetMapping("/lineCharts/{timeRange}")
    public ResponseResult lineCharts(@PathVariable String timeRange) {
        return borrowService.getCountByTimeRange(timeRange);
    }

}
