package com.example.exception;

import com.example.common.ResultCode;

public class CustomException extends RuntimeException {
    private String code;
    private String msg;

    //它扩展并具有两个构造函数来接受错误代码和消息
    //它可用于封装具有特定错误代码和消息的自定义异常
    public CustomException(ResultCode resultCode) {
        this.code = resultCode.code;
        this.msg = resultCode.msg;
    }

    public CustomException(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
