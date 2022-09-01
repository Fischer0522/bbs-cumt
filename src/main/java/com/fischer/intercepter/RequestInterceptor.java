package com.fischer.intercepter;

import com.fischer.exception.BizException;
import com.fischer.exception.ExceptionStatus;
import com.fischer.pojo.UserDO;
import com.fischer.service.JwtService;
import com.fischer.service.RedisService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.Objects;
/**
 * @author fisher
 */
@NoArgsConstructor
@Slf4j
public class RequestInterceptor implements HandlerInterceptor {
    @Autowired
    private JwtService jwtService;
    @Autowired
    private RedisService redisService;
    private final String PREKEY= "loginUser:";
    @Autowired
    public RequestInterceptor (JwtService jwtService,RedisService redisService) {
        this.redisService = redisService;
        this.jwtService = jwtService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        System.out.println(request.getRequestURI());
        String token = request.getHeader("Authorization");
        if (Strings.isEmpty(token)) {
            throw new BizException(401,"未经授权的操作，请登录后重新进行操作");
        }
        UserDO user = jwtService.getUser(token);
        log.info("获取到当前的用户，用户名为："+user.getUsername()+"用户id为"+user.getId());
        Integer userId = user.getId();
        redisService.getKey(userId).orElseThrow(()->new BizException(401,"当前用户已经退出，请重新登录"));
        return true;
    }
}
