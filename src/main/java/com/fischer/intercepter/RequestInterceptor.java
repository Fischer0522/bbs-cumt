package com.fischer.intercepter;

import com.fischer.exception.BizException;
import com.fischer.exception.ExceptionStatus;
import com.fischer.pojo.UserDO;
import com.fischer.service.JwtService;
import com.fischer.service.RedisService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
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
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = request.getHeader("Authorization");
        if (Strings.isEmpty(token)) {
            throw new BizException(ExceptionStatus.UNAUTHORIZED);
        }
        UserDO user = jwtService.getUser(token);
        if (Objects.isNull(user)) {
            throw new BizException(ExceptionStatus.UNAUTHORIZED);
        }
        Integer userId = user.getId();
        redisService.getKey(userId).orElseThrow(()->new BizException(ExceptionStatus.UNAUTHORIZED));
        return true;
    }
}
