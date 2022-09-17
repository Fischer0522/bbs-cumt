package com.fischer.intercepter;

import cn.dev33.satoken.stp.StpUtil;
import com.fischer.exception.BizException;
import com.fischer.mapper.UserMapper;
import com.fischer.pojo.UserDO;
import com.fischer.service.JwtService;
import com.fischer.service.RedisService;
import lombok.NoArgsConstructor;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.Duration;

@NoArgsConstructor
public class RestrainInterceptor implements HandlerInterceptor {


    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private UserMapper userMapper;
    private String KEY_HEAD = "queryFrequency:";
    private int MAX_FREQUENCY = 10;


    @Autowired
    public RestrainInterceptor(StringRedisTemplate StringRedisTemplate){

        this.stringRedisTemplate = StringRedisTemplate;

    }



    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {


        long loginIdAsLong = StpUtil.getLoginIdAsLong();
        UserDO user = userMapper.selectById(loginIdAsLong);
        String username = user.getUsername();
        String key = KEY_HEAD+username;
        String frequency = stringRedisTemplate.opsForValue().get(key);
        if (Strings.isEmpty(frequency)) {
            // 长时间未请求 放行并统计次数
            stringRedisTemplate.opsForValue().set(key,"1");
            Duration duration = Duration.ofSeconds(60);
            stringRedisTemplate.expire(key, duration);
            return true;
        } else if (Integer.parseInt(frequency)< MAX_FREQUENCY) {
            // 请求次数较少，放行
            Integer newFrequency = Integer.parseInt(frequency)+1;
            Long currentTtl = stringRedisTemplate.getExpire(key);
            Duration duration = Duration.ofSeconds(currentTtl);
            stringRedisTemplate.opsForValue().set(key,newFrequency.toString());
            stringRedisTemplate.expire(key,duration);
            return true;
        } else {
            // 请求频率超出一分钟10次 拒绝继续操作
            throw new BizException(400,"请求频率过快");
        }

    }
}
