package com.fischer.service;

import com.fischer.pojo.CommentBO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

@SpringBootTest
public class TestCommentService {

    private CommentService commentService;
    @Autowired
    public TestCommentService(CommentService commentService) {
        this.commentService = commentService;
    }

    @Test
    void testCtreateComment(){
        Integer articleId = 6;
        String body = "说藏话了";
        Integer userId = 1243;
        Optional<CommentBO> comment = commentService.createComment(articleId, body, userId);
        System.out.println(comment.get());
    }

    @Test
    void testdeleteComment(){
        Integer commentId = 4;
        Integer userId = 1242;
        Optional<CommentBO> commentBO = commentService.deleteComment(commentId, userId);
        System.out.println(commentBO.get());
    }

    @Test
    void testGetComments(){
        Integer articleId = 6;
        Integer offset = 0;
        Integer limit = 5;
        Integer orderType = 0;
        //List<CommentBO> comments = commentService.getComments(articleId, offset, limit, orderType);
    }


}
