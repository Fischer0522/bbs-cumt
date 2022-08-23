package com.fischer.exception;


import com.fischer.result.ErrorResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

import static io.lettuce.core.pubsub.PubSubOutput.Type.message;

/**
 * @author fisher
 * @date 2022 7 10
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @Autowired
    StringRedisTemplate redisTemplate;

    @ExceptionHandler(BizException.class)
    public ErrorResult bizExceptionHandler(BizException e) {
        log.error("发生业务异常，具体情况为："+e.getMsg());
        e.printStackTrace();
        return new ErrorResult(e.getCode(), e.getMsg());

    }
    @ExceptionHandler(NullPointerException.class)
    public ErrorResult nullPointerExceptionHandler(NullPointerException e) {
        log.error("发生空指针异常："+e.getMessage());
        e.printStackTrace();
        return new ErrorResult(500, "发生空指针异常");
    }

    @ExceptionHandler(Exception.class)
    public ErrorResult unKnowExceptionHandler(Exception e) {
        log.error("发生未知异常，详情："+e.getMessage());
        e.printStackTrace();
        return new ErrorResult(500,"发生未知异常，请联系管理员");
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ErrorResult validExceptionHandler(MethodArgumentNotValidException e) {

        List<ObjectError> allErrors = e.getBindingResult().getAllErrors();
        String totalMessage = "";
        for(ObjectError error : allErrors) {
            totalMessage += error.getDefaultMessage();
            totalMessage += " ";
        }
        log.error("表单校验未通过，原因为："+message);
        return new ErrorResult(400,totalMessage);

    }

    @ExceptionHandler(LoginException.class)
    public ErrorResult loginExceptionHandler(LoginException e) {
        String key = e.getKey();
        String s = redisTemplate.opsForValue().get(key);
        if(Strings.isNotEmpty(s)) {
            redisTemplate.delete(key);
        }
        return new ErrorResult(500,"登录异常");

    }
}
