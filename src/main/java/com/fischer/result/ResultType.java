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
public class ResultType {
    private Integer code;
    private Object data;
    private String msg;

    public static ResultType success(Object body) {
        return new ResultType(200,body,"OK");
    }
}
