package com.fischer.controller;


import cn.dev33.satoken.annotation.SaCheckDisable;
import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckRole;
import cn.dev33.satoken.annotation.SaIgnore;
import cn.dev33.satoken.stp.StpUtil;
import com.fischer.result.CommonResult;
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
import java.util.Objects;
import java.util.Stack;

/**
 * @author fisher
 */
@Slf4j
@RestController
@RequestMapping("api/articles")
@Validated
@ResponseResult
@SaCheckRole("common-user")
public class ArticleController {
    private ArticleService articleService;
    @Autowired
    ArticleController (ArticleService articleService) {
        this.articleService = articleService;

    }

    @SaCheckLogin
    @PostMapping
    @SaCheckDisable("article")
    ResponseEntity<ArticleDO> createArticle( @Valid @RequestBody NewArticleParam articleParam) {
        long loginIdAsLong = StpUtil.getLoginIdAsLong();
        StpUtil.checkDisable(loginIdAsLong, "article");
        Long userId = StpUtil.getLoginIdAsLong();
        ArticleDO articleDO = articleService.createArticle(articleParam.getTitle(),
                articleParam.getDescription(),
                articleParam.getContent(),
                articleParam.getType(),
                userId).orElseThrow(() -> new BizException(ExceptionStatus.INTERNAL_SERVER_ERROR));
        return ResponseEntity.ok(articleDO);
    }
    @SaCheckLogin
    @DeleteMapping("{articleId}")
    ResponseEntity<ArticleBO> deleteArticle(@PathVariable(value = "articleId") Long articleId) {

        Long userId = StpUtil.getLoginIdAsLong();
        ArticleBO articleBO = articleService.deleteArticle(articleId, userId)
                .orElseThrow(() -> new BizException(ExceptionStatus.ERROR_ADD_ARTICLE_FAIL));
        return ResponseEntity.ok(articleBO);
    }
    @SaIgnore
    @GetMapping("{articleId}")
    ResponseEntity<ArticleBO> getArticle(@PathVariable(value = "articleId") Long articleId) {
        Long userId = null;

        if (StpUtil.isLogin()) {
            userId = StpUtil.getLoginIdAsLong();

        } else {
            log.info("用户匿名访问"+getClass().getName()+":"+"getArticle");
        }
        ArticleBO articleBO = articleService.getArticleById(articleId, userId)
                .orElseThrow(() -> new BizException(ExceptionStatus.ERROR_GET_ARTICLE_FAIL));
        return ResponseEntity.ok(articleBO);

    }
    @SaIgnore
    @GetMapping("exact")
    ResponseEntity<ArticleVO> getArticles(@RequestParam(value = "favoriteBy",required = false) Long favoriteBy,
                                          @RequestParam(value = "author",required = false) Long author,
                                          @RequestParam(value = "type",required = false)Integer type,
                                          @RequestParam(value = "offset",defaultValue = "0") Integer offset,
                                          @RequestParam(value = "limit",defaultValue = "20")Integer limit,
                                          @RequestParam(value = "orderBy",defaultValue = "0") Integer orderBy,
                                          @RequestParam (value = "orderType",defaultValue = "1")Integer orderType) {

        Long userId = null;
        if (StpUtil.isLogin()) {
            userId = StpUtil.getLoginIdAsLong();

        } else {
            log.info("用户匿名访问"+getClass().getName()+":"+"getArticle");
        }
        ArticleVO articles = articleService.getArticles(favoriteBy, author, type, offset, limit, orderBy, orderType, userId);
        return ResponseEntity.ok(articles);

    }
    @SaIgnore
    @GetMapping("fuzzy")
    ResponseEntity<ArticleVO> getArticlesFuzzy(@RequestParam(value = "keyword" ) String keyword,
                                               @RequestParam(value = "offset",defaultValue = "0") Integer offset,
                                               @RequestParam(value = "limit",defaultValue = "20") Integer limit) {
        Long userId = null;

        if (StpUtil.isLogin()) {
            userId = StpUtil.getLoginIdAsLong();

        } else {
            log.info("用户匿名访问"+getClass().getName()+":"+"getArticle");
        }
        ArticleVO articleFuzzy = articleService.getArticleFuzzy(keyword,userId,offset,limit);
        return ResponseEntity.ok(articleFuzzy);


    }

    @SaCheckLogin
    @PostMapping("{articleId}/favorite")
    ResponseEntity<ArticleBO> favoriteArticle(@PathVariable("articleId") Long articleId) {

        Long userId = StpUtil.getLoginIdAsLong();
        ArticleBO articleBO = articleService.favoriteArticle(articleId, userId)
                .orElseThrow(() -> new BizException(ExceptionStatus.ERROR_LIKE_FAIL));
        return ResponseEntity.ok(articleBO);

    }
    @SaCheckLogin
    @DeleteMapping("{articleId}/unfavorite")
    ResponseEntity<ArticleBO> unfavoriteArticle(@PathVariable("articleId") Long articleId) {
        Long userId = StpUtil.getLoginIdAsLong();
        ArticleBO articleBO = articleService.unfavoriteArticle(articleId, userId)
                .orElseThrow(() -> new BizException(ExceptionStatus.ERROR_DISLIKE_FAIL));
        return ResponseEntity.ok(articleBO);
    }

}
