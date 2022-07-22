package com.fischer.exception;


import com.fischer.result.ResponseResult;
import com.fischer.result.ResultType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

/**
 * @author fisher
 * @date 2022 7 10
 */
@RestControllerAdvice

public class GlobalExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);



    @ExceptionHandler(BizException.class)
    public ResponseEntity<ResultType> bizExceptionHandler(BizException e) {
        logger.error("发生业务异常，具体情况为："+e.getMsg());
        e.printStackTrace();
        return ResponseEntity.ok(new ResultType(e.getCode(),null, e.getMsg()));

    }
    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<ResultType> nullPointerExceptionHandler(NullPointerException e) {
        logger.error("发生空指针异常："+e.getMessage());
        e.printStackTrace();
        return new ResponseEntity<>(new ResultType(500,null, "发生空指针异常"), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResultType> unKnowExceptionHandler(Exception e) {
        logger.error("发生未知异常，详情："+e.getMessage());
        e.printStackTrace();
        return new ResponseEntity<>(new ResultType(500,null,"发生未知异常，请联系管理员"), HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
