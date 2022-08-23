package com.fischer.result;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author fisher
 * @date 2022 7 10
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class CommonResult {
    private Integer code;
    private Object data;
    private String msg;

    public static CommonResult success(Object body) {
        return new CommonResult(200,body,"OK");
    }

    public CommonResult(Integer code, String msg) {
        this.code = code;
        this.data = null;
        this.msg = msg;
    }

}
