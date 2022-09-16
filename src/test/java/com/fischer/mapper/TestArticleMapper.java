package com.fischer.mapper;

import com.fischer.pojo.ArticleDO;
import com.fischer.data.MyPage;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class TestArticleMapper {
    ArticleMapper articleMapper;
    @Autowired
    TestArticleMapper (ArticleMapper articleMapper){
        this.articleMapper = articleMapper;
    }
    @Test
    void testGetArticles(){
        MyPage myPage = new MyPage(0,10);
        List<ArticleDO> articles = articleMapper.getArticles(null, null, 0, myPage, 0, 0);
        System.out.println(articles);


    }
    @Test
    void testSelectArticleCount(){
        Integer integer = articleMapper.selectArticleCount(null, 1242, 0);
        System.out.println(integer);
    }

}
