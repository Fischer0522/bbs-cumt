package com.fischer.controller;


import com.fischer.pojo.ArticleVO;
import com.fischer.pojo.UserDO;
import com.fischer.service.ArticleService;
import com.fischer.service.JwtService;
import org.apache.logging.log4j.util.Strings;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @author fisher
 */
@RestController
@RequestMapping("articles")
public class ArticleController {
    private JwtService jwtService;
    private ArticleService articleService;

    ArticleController (JwtService jwtService,
                       ArticleService articleService) {
        this.articleService = articleService;
        this.jwtService = jwtService;
    }

    @GetMapping
    ResponseEntity<ArticleVO> getArticles(@RequestParam(value = "favoriteBy",required = false) Integer favoriteBy,
                                          @RequestParam(value = "author",required = false) Integer author,
                                          @RequestParam(value = "type",required = false)Integer type,
                                          @RequestParam(value = "offset",defaultValue = "0") Integer offset,
                                          @RequestParam(value = "limit",defaultValue = "20")Integer limit,
                                          @RequestParam(value = "orderBy",defaultValue = "0") Integer orderBy,
                                          @RequestParam (value = "orderType",defaultValue = "1")Integer orderType,
                                          @RequestHeader("Authorization") String token) {

        Integer userId = null;
        if(Strings.isNotEmpty(token)) {
            UserDO user = jwtService.getUser(token);
             userId = user.getId();
        }
        ArticleVO articles = articleService.getArticles(favoriteBy, author, type, offset, limit, orderBy, orderType, userId);
        return ResponseEntity.ok(articles);

    }
    @GetMapping("fuzzy")
    ResponseEntity<ArticleVO> getArticlesFuzzy(@RequestParam(value = "keyword" ) String keyword) {
        
    }

}
