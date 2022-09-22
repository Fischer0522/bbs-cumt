package com.fischer.mapper;

import com.fischer.pojo.InfoDO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

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

    @Test
    void updateInfo(){
        List<String> list = new ArrayList<>();
        list.add("https://pic-bed-1309931445.cos.ap-nanjing.myqcloud.com/20170507160427_CPyBR.jpg");
        list.add("https://pic-bed-1309931445.cos.ap-nanjing.myqcloud.com/20170507160504_MYKS3.jpg");
        list.add("https://pic-bed-1309931445.cos.ap-nanjing.myqcloud.com/20170507160547_FQcvE.jpg");
        list.add("https://pic-bed-1309931445.cos.ap-nanjing.myqcloud.com/20170507160618_ATMGm.jpg");
        list.add("https://pic-bed-1309931445.cos.ap-nanjing.myqcloud.com/20170507160645_23nWC.jpg");
        list.add("https://pic-bed-1309931445.cos.ap-nanjing.myqcloud.com/20170507160711_uU3Zy.jpg");
        list.add("https://pic-bed-1309931445.cos.ap-nanjing.myqcloud.com/20170507160739_8tNSP.jpg");
        list.add("https://pic-bed-1309931445.cos.ap-nanjing.myqcloud.com/20170507160820_JuXzL.jpg");
        System.out.println(100%7);
        System.out.println(list.get(7));
        for(int i = 0;i < 108;i++) {
            InfoDO infoDO = infoMapper.selectById(i);
            infoDO.setRandImage(list.get(i%8));
            infoMapper.updateById(infoDO);
        }
    }
}
