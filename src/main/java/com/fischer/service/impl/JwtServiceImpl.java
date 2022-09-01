package com.fischer.service.impl;

import com.fischer.mapper.UserMapper;
import com.fischer.pojo.UserDO;
import com.fischer.service.JwtService;
import com.fischer.exception.BizException;
import com.fischer.exception.ExceptionStatus;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Objects;
import java.util.Optional;

/**
 * @author fisher
 */
@Slf4j
@Service
public class JwtServiceImpl implements JwtService {

    private Integer sessionTime;
    private String secret;
    private UserMapper userMapper;
    @Autowired
    public JwtServiceImpl(
            @Value("${jwt.sessionTime}") Integer sessionTime,
            @Value("${jwt.secret}") String secret,
            UserMapper userMapper){
        this.secret = secret;
        this.userMapper = userMapper;
        this.sessionTime = sessionTime;
    }
    @Override
    public Optional<Integer> getSubFromToken(String token) {

            Jws<Claims> claimsJws = Jwts.parser().setSigningKey(secret).parseClaimsJws(token);
            String sub = claimsJws.getBody().getSubject();

            if(Strings.isEmpty(sub)) {
                log.error("token解析失败");
                return Optional.empty();
        } else {
                log.info("从token中解析出用户id"+sub);
                return Optional.of(Integer.parseInt(sub));
            }

    }

    @Override
    public UserDO getUser(String token) {

        Integer id = getSubFromToken(token)
                .orElseThrow(() -> new BizException(ExceptionStatus.UNAUTHORIZED));
        UserDO userDO = userMapper.selectById(id);
        if(Objects.isNull(userDO)) {
            log.error("token解析出的id在数据库中没有对应的用户");
            throw new BizException(ExceptionStatus.INTERNAL_SERVER_ERROR);
            /*伪造token，服务器装死*/
        }

        return userDO;
    }

    @Override
    public String getToken(UserDO userDO) {
        String token = Jwts.builder()
                .setSubject(userDO.getId().toString())
                .setExpiration(expireTimeFromNow())
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
        if (Strings.isNotEmpty(token)) {
            log.info("token生成成功");
        }
        return token;
    }
    private Date expireTimeFromNow(){
        return new Date(System.currentTimeMillis()+ (long) sessionTime *1000*7);
    }
}
