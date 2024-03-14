package com.feliks.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.feliks.controller.request.BorrowPageRequest;
import com.feliks.dto.BorrowDto;
import com.feliks.entity.Borrow;
import com.feliks.entity.ResponseResult;


/**
 * (Borrow)表服务接口
 *
 * @author makejava
 * @since 2024-03-09 15:16:23
 */
public interface BorrowService extends IService<Borrow> {
    ResponseResult listBorrows();

    ResponseResult listByCondition(BorrowPageRequest borrowPageRequest);

    ResponseResult addBorrow(Borrow borrow);

    ResponseResult getBorrowById(Integer id);

    ResponseResult updateBorrowById(Borrow borrow);

    ResponseResult deleteBorrowById(Integer id);

    ResponseResult returnBook(Integer id);

    ResponseResult getCountByTimeRange(String timeRange);
}
