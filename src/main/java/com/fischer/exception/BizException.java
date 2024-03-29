package com.fischer.exception;


import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @author fischer
 */
@Getter
@NoArgsConstructor
public class BizException extends RuntimeException{
    private Integer code;
    private String msg;
    private Object data;

    public BizException(Integer code, String msg) {
        super(msg);
        this.code = code;
        this.msg = msg;
        this.data = null;
    }
    public BizException(ExceptionStatus exceptionStatus) {
        this.code = exceptionStatus.getValue();
        this.msg = exceptionStatus.getReason();
        this.data = null;
    }
}
