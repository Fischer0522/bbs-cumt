package com.fischer.exception;

import lombok.Getter;


/**
 * @author fischer
 */
@Getter
public enum ExceptionStatus {
    /*基本异常处理信息*/
    OK(200,"OK"),
    /*参数校验失败*/
    BAD_REQUEST(400,"参数校验失败，请重新填写"),
    /*未授权*/
    UNAUTHORIZED(401, "Unauthorized"),
    /*无权限，禁止操作*/
    FORBIDDEN(403,"您无权限这么做"),
    /*资源不存在*/
    NOT_FOUND(404,"该资源已不存在"),
    /*服务器错误*/
    INTERNAL_SERVER_ERROR(500, "服务器异常，请联系管理员")
    ;

    private final int value;
    private final String reason;
    ExceptionStatus(int value, String reason) {
        this.reason = reason;
        this.value = value;

    }
}
