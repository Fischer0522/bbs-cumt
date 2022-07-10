package com.fischer.controller;

import com.fischer.data.CommentParam;
import com.fischer.exception.BizException;
import com.fischer.exception.ExceptionStatus;
import com.fischer.pojo.*;
import com.fischer.result.ResponseResult;
import com.fischer.service.CommentService;
import com.fischer.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**@author fisher
 */
@RestController
@RequestMapping("comments")
@ResponseResult
public class CommentController {

    private CommentService commentService;
    private JwtService jwtService;
    @Autowired
    public CommentController(CommentService commentService,
                             JwtService jwtService) {
        this.commentService = commentService;
        this.jwtService = jwtService;
    }

    @GetMapping("{articleId}")
    ResponseEntity<CommentVO> getComments(@PathVariable(value = "articleId") Integer articleId,
                                          @RequestParam(value = "offset",defaultValue = "0") Integer offset,
                                          @RequestParam(value = "limit",defaultValue = "20") Integer limit,
                                          @RequestParam(value = "orderType",defaultValue = "1")Integer orderType) {

        CommentVO comments = commentService.getComments(articleId, offset, limit, orderType);
        return ResponseEntity.ok(comments);

    }

    @PostMapping
    ResponseEntity<CommentBO> createComment(@RequestBody CommentParam commentParam,
                                            @RequestHeader(value = "Authorization") String token) {
        UserDO user = jwtService.getUser(token);
        Integer userId = user.getId();
        CommentBO commentBO = commentService
                .createComment(commentParam.getArticleId(), commentParam.getBody(), userId)
                .orElseThrow(() -> new BizException(ExceptionStatus.INTERNAL_SERVER_ERROR));
        return ResponseEntity.ok(commentBO);

    }

    @DeleteMapping({"{commentId}"})
    ResponseEntity<CommentBO> deleteComment(@PathVariable(value = "commentId") Integer commentId,
                                            @RequestHeader(value = "Authorization") String token) {
        UserDO user = jwtService.getUser(token);
        Integer userId = user.getId();
        CommentBO commentBO = commentService.deleteComment(commentId, userId)
                .orElseThrow(() -> new BizException(ExceptionStatus.INTERNAL_SERVER_ERROR));
        return ResponseEntity.ok(commentBO);


    }
}
