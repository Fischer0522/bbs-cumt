package com.fischer.exception;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class LoginException extends RuntimeException{
    private String key;
    private String msg;
    private Integer code;
    private String data;

    public LoginException(String key,String msg,Integer code) {
        this.code = code;
        this.data = null;
        this.msg = msg;
        this.key = key;
    }
}
