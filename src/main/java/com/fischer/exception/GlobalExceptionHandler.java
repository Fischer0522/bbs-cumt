package com.fischer.exception;


import com.fischer.result.ErrorResult;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;
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
        return new ErrorResult(500,"发生未知异常，请联系管理员,具体原因为:"+e.getMessage());
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ErrorResult validExceptionHandler(MethodArgumentNotValidException e) {
        e.printStackTrace();

        List<ObjectError> allErrors = e.getBindingResult().getAllErrors();
        String totalMessage = "";
        for(ObjectError error : allErrors) {
            totalMessage += error.getDefaultMessage();
            totalMessage += " ";
        }
        log.error("表单校验未通过，原因为："+message);
        return new ErrorResult(400,totalMessage);

    }


    @ExceptionHandler(ConstraintViolationException.class)
    public ErrorResult updateViolation(HttpServletRequest req, ConstraintViolationException e){
        String temp=e.getMessage();
        String sub=null;
        for(int i=0;i<temp.length()-1;i++){

            String su=temp.substring(i,i+1);
            if(su.equals(":")){
                sub=temp.substring(i+2);
            }
        }
        return new ErrorResult(400,sub);
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

    @ExceptionHandler(ExpiredJwtException.class)
    public ErrorResult JwtExpiredHandler(ExpiredJwtException e) {
        log.warn("当前身份验证已经过期，请重新登录");
        e.printStackTrace();
        return new ErrorResult(500,"当前身份验证已经过期，请重新登录");
    }
}
