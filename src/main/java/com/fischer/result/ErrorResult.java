package com.fischer.result;

import lombok.Data;

@Data
public class ErrorResult {

    private Integer code;

    private Object data;

    private String msg;

    public ErrorResult(Integer code,String msg) {
        this.code = code;
        this.data = null;
        this.msg = msg;

    }
}
