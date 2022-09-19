package com.fischer.controller;

import cn.dev33.satoken.annotation.SaCheckDisable;
import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckRole;
import cn.dev33.satoken.annotation.SaIgnore;
import cn.dev33.satoken.stp.StpUtil;
import com.fischer.data.CommentParam;
import com.fischer.exception.BizException;
import com.fischer.exception.ExceptionStatus;
import com.fischer.pojo.*;
import com.fischer.result.ResponseResult;
import com.fischer.service.CommentService;
import com.fischer.service.JwtService;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.util.annotation.Nullable;

import javax.validation.Valid;

/**@author fisher
 */
@Slf4j
@RestController
@RequestMapping("api/comments")
@ResponseResult
@SaCheckRole("common-user")
public class CommentController {

    private CommentService commentService;

    @Autowired
    public CommentController(CommentService commentService) {
        this.commentService = commentService;

    }
    @SaIgnore
    @GetMapping("{articleId}")
    ResponseEntity<CommentVO> getComments(@PathVariable(value = "articleId") Long articleId,
                                          @RequestParam(value = "offset",defaultValue = "0") Integer offset,
                                          @RequestParam(value = "limit",defaultValue = "20") Integer limit,
                                          @RequestParam(value = "orderType",defaultValue = "1")Integer orderType) {

        Long userId = null;
        if (StpUtil.isLogin()) {
            userId = StpUtil.getLoginIdAsLong();

        } else {
            log.info("用户匿名访问"+getClass().getName()+":"+"getArticle");
        }
        CommentVO comments = commentService.getComments(articleId, offset, limit, orderType,userId);
        return ResponseEntity.ok(comments);

    }
    @SaCheckLogin
    @SaCheckDisable("comment")
    @PostMapping
    ResponseEntity<CommentBO> createComment( @Valid @RequestBody CommentParam commentParam) {
        Long userId = StpUtil.getLoginIdAsLong();
        CommentBO commentBO = commentService
                .createComment(commentParam.getArticleId(), commentParam.getBody(), userId)
                .orElseThrow(() -> new BizException(ExceptionStatus.INTERNAL_SERVER_ERROR));
        return ResponseEntity.ok(commentBO);

    }
    @SaCheckLogin
    @DeleteMapping({"{commentId}"})
    ResponseEntity<CommentBO> deleteComment(@PathVariable(value = "commentId") Long commentId) {
        Long userId = StpUtil.getLoginIdAsLong();
        CommentBO commentBO = commentService.deleteComment(commentId, userId)
                .orElseThrow(() -> new BizException(ExceptionStatus.INTERNAL_SERVER_ERROR));
        return ResponseEntity.ok(commentBO);


    }

    @SaCheckLogin
    @PostMapping("{commentId}/favorite")
    ResponseEntity<CommentBO> favoriteComment(@PathVariable(value = "commentId") Long commentId) {

        Long userId = StpUtil.getLoginIdAsLong();
        CommentBO commentBO = commentService.favoriteComment(commentId, userId)
                .orElseThrow(() -> new BizException(ExceptionStatus.INTERNAL_SERVER_ERROR));
        return ResponseEntity.ok(commentBO);

    }

    @SaCheckLogin
    @DeleteMapping("{commentId}/favorite")
    ResponseEntity<CommentBO> unfavoriteCount(@PathVariable(value = "commentId") Long commentId){
        Long userId = StpUtil.getLoginIdAsLong();
        CommentBO commentBO = commentService.unfavoriteComment(commentId, userId)
                .orElseThrow(() -> new BizException(ExceptionStatus.INTERNAL_SERVER_ERROR));
        return ResponseEntity.ok(commentBO);
    }


}
