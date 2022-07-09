package com.fischer.service;

import com.fischer.pojo.UserDO;
import com.fischer.service.impl.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

@SpringBootTest
public class TestUserService {
    UserServiceImpl userService;
    @Autowired
    public TestUserService(UserServiceImpl userService){
        this.userService=userService;

    }
    @Test
    void TestCreateUser(){
        UserDO userDO = userService.createUser("1809327837@qq.com");
        System.out.println(userDO);
    }
    @Test
    void TestGetUserById(){
        Integer id = 1243;
        Optional<UserDO> userDO = userService.getUserById(id);
        System.out.println(userDO.get());
    }
    @Test
    void TestSearchUser(){
        String username = "的";
        List<UserDO> userDOS = userService.searchUser(username);
        System.out.println(userDOS);

    }
    @Test
    void TestGetUserCount(){
        Integer userCount = userService.getUserCount();
        String a ="123";
        a="456";
        System.out.println(a);
        System.out.println(userCount);
    }


}
