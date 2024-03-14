package com.feliks.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.feliks.annotation.mySystemlog;
import com.feliks.constans.SystemConstants;
import com.feliks.controller.request.BorrowPageRequest;
import com.feliks.entity.*;
import com.feliks.enums.AppHttpCodeEnum;
import com.feliks.mapper.BorrowMapper;
import com.feliks.po.BorrowReturnCountPO;
import com.feliks.service.BookService;
import com.feliks.service.BorrowService;
import com.feliks.service.ReturnbookService;
import com.feliks.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.*;

import static com.feliks.constans.SystemConstants.FIFTEEN_DAYS;

/**
 * (Borrow)表服务实现类
 *
 * @author makejava
 * @since 2024-03-09 15:16:23
 */
@Service
public class BorrowServiceImpl extends ServiceImpl<BorrowMapper, Borrow> implements BorrowService {

    @Override
    @mySystemlog(xxbusinessName = "查询所有Borrow")//接口描述，用于'日志记录'功能
    public ResponseResult listBorrows() {
        LambdaQueryWrapper<Borrow> queryWrapper = new LambdaQueryWrapper<>();
        List<Borrow> borrows = list(queryWrapper);
        for (Borrow borrow : borrows) {
            //account
            String userUsername = borrow.getUserUsername();
            LambdaQueryWrapper<User> userLambdaQueryWrapper = new LambdaQueryWrapper<>();
            userLambdaQueryWrapper.eq(User::getUsername, userUsername);
            User user = userService.getOne(userLambdaQueryWrapper);
            borrow.setAccount(user.getAccount());

            //nums
            String bookNumber = borrow.getBookNumber();
            LambdaQueryWrapper<Book> bookLambdaQueryWrapper = new LambdaQueryWrapper<>();
            bookLambdaQueryWrapper.eq(Book::getBookNumber, bookNumber);
            Book book = bookService.getOne(bookLambdaQueryWrapper);
            borrow.setNums(book.getNums());
        }
        return ResponseResult.okResult(borrows);
    }

    private LocalDate dateToLocalDate(Date date) {
        Instant instant = date.toInstant();
        ZoneId zoneId = ZoneId.systemDefault();

        // atZone()方法返回在指定时区从此Instant生成的ZonedDateTime。
        LocalDate localDate = instant.atZone(zoneId).toLocalDate();
        return localDate;
    }

    @Override
    @mySystemlog(xxbusinessName = "根据条件查询Borrow")//接口描述，用于'日志记录'功能
    public ResponseResult listByCondition(BorrowPageRequest borrowPageRequest) {
        LambdaQueryWrapper<Borrow> queryWrapper = new LambdaQueryWrapper<>();
        List<Borrow> borrowList = list(queryWrapper);
        for (Borrow borrow : borrowList) {
            //当前时间
            Date now = new Date();
            LocalDate nowLocalDate = dateToLocalDate(now);
            //借书的时间
            Date createTime = borrow.getCreateTime();
            LocalDate createTimeLocalDate = dateToLocalDate(createTime);
            //计算过去几天
            long between = ChronoUnit.DAYS.between(createTimeLocalDate, nowLocalDate);
            if (borrow.getStatus().equals(SystemConstants.STATUS_NOT_RETURNED) && (int) between > 1) {
                //设置借书天数
                borrow.setDays((int) between);
            }
            updateById(borrow);
        }
        Page<Borrow> borrowPage = new Page<>(borrowPageRequest.getPageNum(), borrowPageRequest.getPageSize());
        queryWrapper.like(StringUtils.hasText(borrowPageRequest.getBookName()), Borrow::getBookName, borrowPageRequest.getBookName());
        queryWrapper.like(StringUtils.hasText(borrowPageRequest.getBookNumber()), Borrow::getBookNumber, borrowPageRequest.getBookNumber());
        queryWrapper.like(StringUtils.hasText(borrowPageRequest.getUserName()), Borrow::getUserName, borrowPageRequest.getUserName());
        queryWrapper.like(StringUtils.hasText(borrowPageRequest.getUserUsername()), Borrow::getUserUsername, borrowPageRequest.getUserUsername());
        queryWrapper.like(StringUtils.hasText(borrowPageRequest.getStatus()), Borrow::getStatus, borrowPageRequest.getStatus());
        queryWrapper.orderByDesc(Borrow::getId);
        page(borrowPage, queryWrapper);
        return ResponseResult.okResult(borrowPage);
    }

    @Autowired
    private UserService userService;

    @Autowired
    private BookService bookService;

    @Override
    @mySystemlog(xxbusinessName = "添加Borrow")//接口描述，用于'日志记录'功能
    public ResponseResult addBorrow(Borrow borrow) {
        System.out.println(borrow.toString());
        // 唯一：userUsername、bookNumber
        //1.校验用户的积分是否足够
        String userUsername = borrow.getUserUsername();
        LambdaQueryWrapper<User> userLambdaQueryWrapper = new LambdaQueryWrapper<>();
        userLambdaQueryWrapper.eq(User::getUsername, userUsername);
        User borrowUser = userService.getOne(userLambdaQueryWrapper);
        Integer account = borrowUser.getAccount();
        Integer score = borrow.getScore();
        if (account < score) {
            return ResponseResult.errorResult(AppHttpCodeEnum.INSUFFICIENT_BALANCE);
        }

        //2.校验剩余图书是否足够
        String bookNumber = borrow.getBookNumber();
        LambdaQueryWrapper<Book> bookLambdaQueryWrapper = new LambdaQueryWrapper<>();
        bookLambdaQueryWrapper.eq(Book::getBookNumber, bookNumber);
        Book borrowBook = bookService.getOne(bookLambdaQueryWrapper);
        //实际剩余的书籍数量
        Integer borrowBookNums = borrowBook.getNums();
        if (borrowBookNums == 0) {
            return ResponseResult.errorResult(AppHttpCodeEnum.INSUFFICIENT_NUMBER);
        }

        //借出状态、借出天数、归还日期的校验
        //1.每个用户可借图书为5本，超出5本则要先归还才可借出
        if (borrowUser.getBorrowBook() >= 5) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PLEASE_RETURN);
        }

        //2.校验用户是否有书本借出超
        //如果当前选择借书的用户已经存在借过15天书数量则对已借的书籍进行归还日期校验
        if ((borrowUser.getBorrowBook() > 0 && borrowUser.getBorrowBook() <= 5)) {
            LambdaQueryWrapper<Borrow> borrowLambdaQueryWrapper = new LambdaQueryWrapper<>();
            //当前用户未归还书籍的借阅信息
            borrowLambdaQueryWrapper.eq(Borrow::getUserUsername, borrowUser.getUsername());
            borrowLambdaQueryWrapper.eq(Borrow::getStatus, SystemConstants.STATUS_NOT_RETURNED);
            //borrowList是当前用户所有未归还的书籍借阅信息
            List<Borrow> borrowList = list(borrowLambdaQueryWrapper);
            for (Borrow notReturnBorrow : borrowList) {
                //
                if (notReturnBorrow.getReturnDate() == null && notReturnBorrow.getDays() >= 15) {
                    return ResponseResult.errorResult(AppHttpCodeEnum.PLEASE_RETURN);
                }
            }
        }

        //走到这里的都是用户积分足够、书籍余量足够、当前用户借书不足5本、已借书籍未到归还日期或没有借过书的用户
        //借出状态：未归还、已归还
        //设置当前借出书籍为未归还
        borrow.setStatus(SystemConstants.STATUS_NOT_RETURNED);
        //每本书的借出天数为15天，需要提前提醒用户，超过15天后将扣除所借书籍的双倍积分
        borrow.setDays(1);
        //都足够的情况
        borrowUser.setAccount(account - score);
        borrowBook.setNums(borrowBookNums - 1);
        borrowUser.setBorrowBook(borrowUser.getBorrowBook() + 1);
        userService.updateById(borrowUser);
        bookService.updateById(borrowBook);
        save(borrow);
        //获取当前时间
        long createTime = borrow.getCreateTime().getTime();//null
        //在当前时间加15天
        //设置书籍归还日期
        //归还日期：borrow的createTime加上15天
        borrow.setReturnDate(new Date(createTime + FIFTEEN_DAYS));
        updateById(borrow);
        return ResponseResult.okResult();
    }

    @Override
    @mySystemlog(xxbusinessName = "根据id获取Borrow")//接口描述，用于'日志记录'功能
    public ResponseResult getBorrowById(Integer id) {
        Borrow borrow = getById(id);
        //account
        String userUsername = borrow.getUserUsername();
        LambdaQueryWrapper<User> userLambdaQueryWrapper = new LambdaQueryWrapper<>();
        userLambdaQueryWrapper.eq(User::getUsername, userUsername);
        User user = userService.getOne(userLambdaQueryWrapper);
        borrow.setAccount(user.getAccount());

        //nums
        String bookNumber = borrow.getBookNumber();
        LambdaQueryWrapper<Book> bookLambdaQueryWrapper = new LambdaQueryWrapper<>();
        bookLambdaQueryWrapper.eq(Book::getBookNumber, bookNumber);
        Book book = bookService.getOne(bookLambdaQueryWrapper);
        borrow.setNums(book.getNums());

        return ResponseResult.okResult(borrow);
    }

    @Override
    @mySystemlog(xxbusinessName = "根据id更新Borrow")//接口描述，用于'日志记录'功能
    public ResponseResult updateBorrowById(Borrow borrow) {
        //account
        String userUsername = borrow.getUserUsername();
        LambdaQueryWrapper<User> userLambdaQueryWrapper = new LambdaQueryWrapper<>();
        userLambdaQueryWrapper.eq(User::getUsername, userUsername);
        User user = userService.getOne(userLambdaQueryWrapper);
        borrow.setAccount(user.getAccount());

        //nums
        String bookNumber = borrow.getBookNumber();
        LambdaQueryWrapper<Book> bookLambdaQueryWrapper = new LambdaQueryWrapper<>();
        bookLambdaQueryWrapper.eq(Book::getBookNumber, bookNumber);
        Book book = bookService.getOne(bookLambdaQueryWrapper);
        borrow.setNums(book.getNums());

        updateById(borrow);
        return ResponseResult.okResult();
    }

    @Override
    @mySystemlog(xxbusinessName = "根据id删除Borrow")//接口描述，用于'日志记录'功能
    public ResponseResult deleteBorrowById(Integer id) {
        removeById(id);
        return ResponseResult.okResult();
    }

    @Autowired
    private ReturnbookService returnbookService;

    //还书
    @Override
    @mySystemlog(xxbusinessName = "还书")//接口描述，用于'日志记录'功能
    public ResponseResult returnBook(Integer id) {
        LambdaQueryWrapper<Borrow> borrowLambdaQueryWrapper = new LambdaQueryWrapper<>();
        borrowLambdaQueryWrapper.eq(Borrow::getId, id);
        Borrow borrow = getOne(borrowLambdaQueryWrapper);
        if (borrow.getStatus().equals(SystemConstants.STATUS_RETURNED)) {
            return ResponseResult.errorResult(AppHttpCodeEnum.THE_BOOK_HAS_BEEN_RETURNED);
        }
        //获取当前借出书籍的标准码
        String bookNumber = borrow.getBookNumber();
        //查询出这本书
        LambdaQueryWrapper<Book> bookLambdaQueryWrapper = new LambdaQueryWrapper<>();
        bookLambdaQueryWrapper.eq(Book::getBookNumber, bookNumber);
        Book borrowBook = bookService.getOne(bookLambdaQueryWrapper);

        //获取当前借书人的id
        Integer userId = borrow.getUserId();
        //查询出借书用户的信息
        LambdaQueryWrapper<User> userLambdaQueryWrapper = new LambdaQueryWrapper<>();
        userLambdaQueryWrapper.eq(User::getId, userId);
        User borrowUser = userService.getOne(userLambdaQueryWrapper);


        //如果超过15天后还书则需要额外扣除两倍于当前所借书籍的积分
        if (borrow.getDays() > 15) {
            //有足够的积分扣除
            if (borrowUser.getAccount() - borrow.getScore() * 2 >= 0) {
                borrowUser.setAccount(borrowUser.getAccount() - borrow.getScore() * 2);
            } else {
                //不够就清零
                borrowUser.setAccount(0);
            }
        }

        //该书的放回库存，库存加一
        borrowBook.setNums(borrowBook.getNums() + 1);
        //用户借用书籍减一
        borrowUser.setBorrowBook(borrowUser.getBorrowBook() - 1);
        //设置状态为已归还
        borrow.setStatus(SystemConstants.STATUS_RETURNED);
        Date now = new Date();
        //还书日期设置为当前
        borrow.setReturnDate(now);
        //设置returnbook表的createTime，表示书本归还的时间
        Returnbook returnbook = new Returnbook();
        returnbook.setCreateTime(now);
        returnbookService.save(returnbook);
        updateById(borrow);
        userService.updateById(borrowUser);
        bookService.updateById(borrowBook);
        return ResponseResult.okResult();
    }

    //获取前端传入的DateTime列表
    private List<DateTime> getDateRange(String timeRange) {
        Date today = new Date();
        List<DateTime> dateRange;
        switch (timeRange) {
            case "week":
                dateRange = DateUtil.rangeToList(DateUtil.offsetDay(today, -6), today, DateField.DAY_OF_WEEK);
                break;
            case "month":
                dateRange = DateUtil.rangeToList(DateUtil.offsetDay(today, -29), today, DateField.DAY_OF_MONTH);
                break;
            case "month2":
                dateRange = DateUtil.rangeToList(DateUtil.offsetDay(today, -59), today, DateField.DAY_OF_MONTH);
                break;
            case "month3":
                dateRange = DateUtil.rangeToList(DateUtil.offsetDay(today, -89), today, DateField.DAY_OF_MONTH);
                break;
            default:
                dateRange = new ArrayList<>();
        }
        return dateRange;
    }

    @Autowired
    private BorrowMapper borrowMapper;

    // timeRange: week month month2 month3
    @Override
    @mySystemlog(xxbusinessName = "图书借还情况统计")//接口描述，用于'日志记录'功能
    public ResponseResult getCountByTimeRange(String timeRange) {
        Map<String, Object> map = new HashMap<>();
        List<DateTime> dateRange = getDateRange(timeRange);
        List<String> dateStrRange = datetimeToDateStr(dateRange);

        List<BorrowReturnCountPO> borrowCount = borrowMapper.getCountByTimeRange(timeRange, 1);
        List<BorrowReturnCountPO> returnCount = borrowMapper.getCountByTimeRange(timeRange, 2);

        map.put("date", dateRange);
        map.put("borrowCount", countList(borrowCount, dateStrRange));
        map.put("returnCount", countList(returnCount, dateStrRange));
        return ResponseResult.okResult(map);
    }

    private List<String> datetimeToDateStr(List<DateTime> dateRange) {
        List<String> dateStrRange = new ArrayList<>();
        for (DateTime dateTime : dateRange) {
            dateStrRange.add(dateTime.toDateStr());
        }
        return dateStrRange;
    }
    private List<Integer> countList(List<BorrowReturnCountPO> countPOList, List<String> dateRange) {
        List<Integer> list = CollUtil.newArrayList();
        if (CollUtil.isEmpty(countPOList)) {
            return list;
        }
        for (String date : dateRange) {
            Integer count = countPOList.stream().filter(countPO -> date.equals(countPO.getDate()))
                    .map(BorrowReturnCountPO::getCount).findFirst().orElse(0);
            list.add(count);
        }
        return list;
    }
}



























