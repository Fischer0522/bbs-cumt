package com.fischer.service;

import com.fischer.pojo.ArticleDO;
import com.fischer.pojo.ArticleBO;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

@SpringBootTest
@Slf4j
public class TestArticleService {
    private ArticleService articleService;
    @Autowired
    TestArticleService (ArticleService articleService){
        this.articleService = articleService;
    }

    @Test
    void testgetArticles(){

        Integer author = 1242;
        Integer type = 0;
        Integer offset = 0;
        Integer limit = 5;
        Integer orderBy = 1;
        Integer orderType = 2;
        Integer userId = 1243;
        //List<ArticleBO> articles = articleService.getArticles(null, null, type, 0, 5, 0, 1, userId);
       // System.out.println(articles);

    }



    @Test
    void testGetArticleFuzzy(){
       // articleService.getArticleFuzzy("丁真",1242);
    }


    @Test
    void testUnFavoriteArticle(){
        //articleService.unfavoriteArticle(6,1242);


    }




}
