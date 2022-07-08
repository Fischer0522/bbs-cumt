package com.fischer.mapper;

import com.fischer.pojo.AdjDO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class TestAdjMapper {
    private AdjMapper adjMapper;
    @Autowired
    TestAdjMapper(AdjMapper adjMapper){
        this.adjMapper = adjMapper;
    }

    @Test
    void testGetAdj(){
        AdjDO adj = adjMapper.getAdj();
        System.out.println(adj);
    }

}
