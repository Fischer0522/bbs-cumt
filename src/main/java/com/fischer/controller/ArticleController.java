package com.fischer.controller;


import com.fischer.exception.BizException;
import com.fischer.exception.ExceptionStatus;
import com.fischer.param.NewArticleParam;
import com.fischer.pojo.ArticleBO;
import com.fischer.pojo.ArticleDO;
import com.fischer.pojo.ArticleVO;
import com.fischer.pojo.UserDO;
import com.fischer.service.ArticleService;
import com.fischer.service.JwtService;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
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
    @Autowired
    ArticleController (JwtService jwtService,
                       ArticleService articleService) {
        this.articleService = articleService;
        this.jwtService = jwtService;
    }
    @PostMapping
    ResponseEntity<ArticleDO> createArticle(@RequestBody NewArticleParam articleParam,
                                            @RequestHeader("Authorization") String token) {
        UserDO user = jwtService.getUser(token);
        Integer userId = user.getId();
        ArticleDO articleDO = articleService.createArticle(articleParam.getTitle(),
                articleParam.getDescription(),
                articleParam.getContent(),
                articleParam.getType(),
                userId).orElseThrow(() -> new BizException(ExceptionStatus.INTERNAL_SERVER_ERROR));
        return ResponseEntity.ok(articleDO);
    }
    @DeleteMapping("{articleId}")
    ResponseEntity<ArticleBO> deleteArticle(@PathVariable(value = "articleId") Integer articleId,
                                            @RequestHeader(value = "Authorization") String token) {
        UserDO user = jwtService.getUser(token);
        Integer userId = user.getId();
        ArticleBO articleBO = articleService.deleteArticle(articleId, userId)
                .orElseThrow(() -> new BizException(ExceptionStatus.INTERNAL_SERVER_ERROR));
        return ResponseEntity.ok(articleBO);
    }
    @GetMapping("exact")
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
    ResponseEntity<ArticleVO> getArticlesFuzzy(@RequestParam(value = "keyword" ) String keyword,
                                               @RequestHeader("Authorization") String token,
                                               @RequestParam(value = "offset",defaultValue = "0") Integer offset,
                                               @RequestParam(value = "limit",defaultValue = "20") Integer limit) {
        Integer userId = null;
        if(Strings.isNotEmpty(token)) {
            UserDO user = jwtService.getUser(token);
            userId = user.getId();
        }
        ArticleVO articleFuzzy = articleService.getArticleFuzzy(keyword,userId,offset,limit);
        return ResponseEntity.ok(articleFuzzy);


    }


    @PostMapping("{articleId}/favorite")
    ResponseEntity<ArticleBO> favoriteArticle(@PathVariable("articleId") Integer articleId,
                                              @RequestHeader("Authorization") String token) {
        UserDO user = jwtService.getUser(token);
        Integer userId = user.getId();
        ArticleBO articleBO = articleService.favoriteArticle(articleId, userId)
                .orElseThrow(() -> new BizException(ExceptionStatus.INTERNAL_SERVER_ERROR));
        return ResponseEntity.ok(articleBO);

    }

    @DeleteMapping("{articleId}/unfavorite")
    ResponseEntity<ArticleBO> unfavoriteArticle(@PathVariable("articleId") Integer articleId,
                                                @RequestHeader("Authorization") String token) {
        UserDO user = jwtService.getUser(token);
        Integer userId = user.getId();
        ArticleBO articleBO = articleService.unfavoriteArticle(articleId, userId)
                .orElseThrow(() -> new BizException(ExceptionStatus.INTERNAL_SERVER_ERROR));
        return ResponseEntity.ok(articleBO);
    }

}
