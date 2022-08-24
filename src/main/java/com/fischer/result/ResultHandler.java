package com.fischer.result;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Objects;

@ControllerAdvice
@Slf4j
public class ResultHandler implements ResponseBodyAdvice<Object> {

    public static final String RESPONSE_RESULT = "RESPONSE_RESULT";
    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = requestAttributes.getRequest();
        ResponseResult attribute = (ResponseResult) request.getAttribute(RESPONSE_RESULT);
        return attribute != null;
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter methodParameter, MediaType mediaType, Class<? extends HttpMessageConverter<?>> aClass, ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse) {

        Method method = methodParameter.getMethod();
        ResponseResult annotation = method.getAnnotation(ResponseResult.class);
        if (Objects.isNull(annotation)) {
            if (body instanceof ErrorResult) {
                /*if (((ErrorResult) body).getCode() == 500) {
                    return new ResponseEntity<ErrorResult>((ErrorResult) body, HttpStatus.INTERNAL_SERVER_ERROR);
                }*/
                return body;
            }
           return CommonResult.success(body);
        } else {
            Integer code = Integer.parseInt(annotation.code());
            String msg = annotation.msg();
            return new CommonResult(code,body,msg);
        }

    }
}
