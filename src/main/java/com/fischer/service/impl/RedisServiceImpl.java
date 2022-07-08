package com.fischer.service.impl;

import com.fischer.mapper.UserMapper;
import com.fischer.pojo.UserDO;
import com.fischer.service.RedisService;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.time.Duration;
@Service
@NoArgsConstructor
public class RedisServiceImpl implements RedisService {
    private StringRedisTemplate stringRedisTemplate;
    private UserMapper userMapper;
    private String preKey;
    private long outDate = 7;
    @Autowired
    public RedisServiceImpl(StringRedisTemplate stringRedisTemplate,UserMapper userMapper) {
        this.stringRedisTemplate =stringRedisTemplate;
        this.userMapper = userMapper;
        this.preKey = "loginUser:";
    }
    @Transactional(rollbackFor = {Exception.class})
    @Override
    public void saveKey(Integer id) {
        String key = preKey+id;
        String username = userMapper.selectById(id).getUsername();
        stringRedisTemplate.opsForValue().set(key,username);
        Duration sessionTime = Duration.ofDays(outDate);
        stringRedisTemplate.expire(key,sessionTime);
    }

    @Override
    public void deleteKey(Integer id) {
        String key = preKey+id;
        stringRedisTemplate.delete(key);

    }

    @Override
    public String getKey(Integer id) {
        String key = preKey+id;
        String s = stringRedisTemplate.opsForValue().get(key);
        return s;
    }
}
