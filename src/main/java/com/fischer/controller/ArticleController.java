package com.fischer.controller;


import com.fischer.result.ResponseResult;
import com.fischer.exception.BizException;
import com.fischer.exception.ExceptionStatus;
import com.fischer.data.NewArticleParam;
import com.fischer.pojo.ArticleBO;
import com.fischer.pojo.ArticleDO;
import com.fischer.pojo.ArticleVO;
import com.fischer.pojo.UserDO;
import com.fischer.service.ArticleService;
import com.fischer.service.JwtService;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.util.annotation.Nullable;

import javax.validation.Valid;
import java.util.Stack;

/**
 * @author fisher
 */
@Slf4j
@RestController
@RequestMapping("articles")
@Validated
@ResponseResult
public class ArticleController {
    private JwtService jwtService;
    private ArticleService articleService;
    private final String AUTHORIZATION = "Authorization";
    @Autowired
    ArticleController (JwtService jwtService,
                       ArticleService articleService) {
        this.articleService = articleService;
        this.jwtService = jwtService;
    }


    @PostMapping
    ResponseEntity<ArticleDO> createArticle( @Valid @RequestBody NewArticleParam articleParam,
                                            @RequestHeader(AUTHORIZATION) String token) {


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
                                            @RequestHeader(value = AUTHORIZATION) String token) {
        UserDO user = jwtService.getUser(token);
        Integer userId = user.getId();
        ArticleBO articleBO = articleService.deleteArticle(articleId, userId)
                .orElseThrow(() -> new BizException(ExceptionStatus.INTERNAL_SERVER_ERROR));
        return ResponseEntity.ok(articleBO);
    }

    @GetMapping("{articleId}")
    ResponseEntity<ArticleBO> getArticle(@PathVariable(value = "articleId") Integer articleId,
                                         @Nullable @RequestHeader(value = AUTHORIZATION) String token) {
        Integer userId = null;
        if (Strings.isNotEmpty(token)) {
            UserDO user = jwtService.getUser(token);
            userId = user.getId();
        } else {
            log.info("用户匿名访问"+getClass().getName()+":"+"getArticle");
        }
        ArticleBO articleBO = articleService.getArticleById(articleId, userId)
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
                                          @Nullable @RequestHeader(AUTHORIZATION) String token) {

        Integer userId = null;
        if(Strings.isNotEmpty(token)) {
            UserDO user = jwtService.getUser(token);
             userId = user.getId();
        } else {
            log.info("用户匿名访问"+getClass().getName()+":"+"getArticles");
        }
        ArticleVO articles = articleService.getArticles(favoriteBy, author, type, offset, limit, orderBy, orderType, userId);
        return ResponseEntity.ok(articles);

    }

    @GetMapping("fuzzy")
    ResponseEntity<ArticleVO> getArticlesFuzzy(@RequestParam(value = "keyword" ) String keyword,
                                               @RequestHeader(AUTHORIZATION) String token,
                                               @RequestParam(value = "offset",defaultValue = "0") Integer offset,
                                               @RequestParam(value = "limit",defaultValue = "20") Integer limit) {
        Integer userId = null;
        if(Strings.isNotEmpty(token)) {
            UserDO user = jwtService.getUser(token);
            userId = user.getId();
        } else {
            log.info("用户匿名访问"+getClass().getName()+":"+"getArticleFuzzy");
        }
        ArticleVO articleFuzzy = articleService.getArticleFuzzy(keyword,userId,offset,limit);
        return ResponseEntity.ok(articleFuzzy);


    }


    @PostMapping("{articleId}/favorite")
    ResponseEntity<ArticleBO> favoriteArticle(@PathVariable("articleId") Integer articleId,
                                              @RequestHeader(AUTHORIZATION) String token) {
        UserDO user = jwtService.getUser(token);
        Integer userId = user.getId();
        ArticleBO articleBO = articleService.favoriteArticle(articleId, userId)
                .orElseThrow(() -> new BizException(ExceptionStatus.INTERNAL_SERVER_ERROR));
        return ResponseEntity.ok(articleBO);

    }

    @DeleteMapping("{articleId}/unfavorite")
    ResponseEntity<ArticleBO> unfavoriteArticle(@PathVariable("articleId") Integer articleId,
                                                @RequestHeader(AUTHORIZATION) String token) {
        UserDO user = jwtService.getUser(token);
        Integer userId = user.getId();
        ArticleBO articleBO = articleService.unfavoriteArticle(articleId, userId)
                .orElseThrow(() -> new BizException(ExceptionStatus.INTERNAL_SERVER_ERROR));
        return ResponseEntity.ok(articleBO);
    }

}
