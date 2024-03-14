package com.feliks.enums;

public enum AppHttpCodeEnum {
    // 成功
    SUCCESS(200, "操作成功"),
    IS_ADMIN(201, "超级管理员不可删除！"),
    ADMIN_IS_NOT_EXIST(202, "用户不存在，可能已被删除，请联系管理员！"),
    // 登录
    NEED_LOGIN(401, "需要登录后操作"),
    NO_OPERATOR_AUTH(403, "无权限操作"),
    SYSTEM_ERROR(500, "出现错误"),
    USERNAME_EXIST(501, "用户名已存在"),
    PHONENUMBER_EXIST(502, "手机号已存在"),
    CATEGORYNAME_EXIST(503, "分类名已存在"),
    LOGIN_ERROR(504, "用户名或密码错误"),
    REQUIRE_NAME(505, "必须填写姓名"),
    REQUIRE_USERNAME(506, "必需填写用户名"),
    REQUIRE_AGE(507, "必须填写年龄"),
    REQUIRE_SEX(508, "必须填写性别"),
    REQUIRE_PHONE(509, "必须填写联系方式"),
    REQUIRE_ADDRESS(510, "必须填写地址"),
    INSUFFICIENT_BALANCE(511, "账户积分不足"),
    INSUFFICIENT_NUMBER(511, "书籍数量不足"),
    BOOKNUMBER_EXIST(512, "书籍标准码重复"),
    PLEASE_RETURN(513, "请归还书籍后再进行借阅"),
    THE_BOOK_HAS_BEEN_RETURNED(514, "书籍已归还，请不要重复还书"),
    FILE_UPLOAD_ERROR(515, "文件上传失败"),
    FILE_DOWNLOAD_ERROR(516, "文件下载失败"),
    ;
    int code;
    String msg;

    AppHttpCodeEnum(int code, String errorMessage) {
        this.code = code;
        this.msg = errorMessage;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
