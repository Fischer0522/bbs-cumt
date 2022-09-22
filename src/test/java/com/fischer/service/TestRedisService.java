package com.fischer.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class TestRedisService {
    private RedisService redisService;
    @Autowired
    public TestRedisService(RedisService redisService){
        this.redisService = redisService;
    }

    @Test
    void testGetKey(){
        Integer id = 1242;
        //System.out.println(key);
    }


}
