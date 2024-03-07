package com.itmk.web.exception;

/**
 * @Author java实战基地
 * @Version 2383404558
 */
public enum  BusinessExceptionEnum {
    SERVER_ERROR(500, "后端接口报错！")
    ;

    private Integer code;
    private String message;

    BusinessExceptionEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}