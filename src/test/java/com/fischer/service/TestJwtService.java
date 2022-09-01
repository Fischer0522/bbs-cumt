package com.fischer.service;

import com.fischer.mapper.UserMapper;
import com.fischer.pojo.UserDO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;

import java.util.Optional;

@SpringBootTest
public class TestJwtService {
    private UserMapper userMapper;
    private JwtService jwtService;
    @Autowired
    public TestJwtService(UserMapper userMapper,JwtService jwtService){
        this.userMapper = userMapper;
        this.jwtService = jwtService;
    }


    @Test
    void testGetUser(){
        String token = null;
        UserDO user = jwtService.getUser(token);
        System.out.println(user);

    }
    @Test
    void testGetToken(){
        Integer userId = 1242;
        UserDO userDO = userMapper.selectById(userId);
        String token = jwtService.getToken(userDO);
        System.out.println(token);

    }
    @Test
    void testGetSub(){
        String token = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxMjQyIiwiZXhwIjoxNjU3ODk4NjA5fQ.IEWqhKXPpJphFaUDytAaBdbdZE4tqMUXLLP02SPKzX0";
        Optional<Integer> subFromToken = jwtService.getSubFromToken(token);
        System.out.println(subFromToken.get());
    }
}
