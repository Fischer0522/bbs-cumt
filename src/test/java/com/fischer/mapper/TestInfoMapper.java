package com.fischer.mapper;

import com.fischer.pojo.InfoDO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class TestInfoMapper {
    private InfoMapper infoMapper;
    @Autowired
    TestInfoMapper(InfoMapper infoMapper){
        this.infoMapper = infoMapper;
    }
    @Test
    void testGetRand(){
        InfoDO info = infoMapper.getInfo();
        System.out.println(info);


    }
}
