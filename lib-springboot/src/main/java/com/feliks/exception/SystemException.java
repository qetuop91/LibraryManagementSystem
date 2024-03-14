package com.feliks.exception;

import com.feliks.enums.AppHttpCodeEnum;

// 统一异常处理
public class SystemException extends RuntimeException {
    private int code;
    private String msg;

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    // 定义一个构造方法，接收的参数是枚举类型，AppHttpCodeEnum是我们在enums里的枚举类
    public SystemException(AppHttpCodeEnum httpCodeEnum) {
        super(httpCodeEnum.getMsg());
        // 把某个枚举类里面的code和msg赋值给异常对象
        this.code = httpCodeEnum.getCode();
        this.msg = httpCodeEnum.getMsg();
    }
}
