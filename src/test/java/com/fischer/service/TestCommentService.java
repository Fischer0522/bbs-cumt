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
        Runnable create =() -> {
          commentService.createComment(8,"说藏话了",1242);
        };

        Runnable get = () -> {
            System.out.println(commentService.getComments(8, 0, 1000, 1));
            System.out.println(commentService.getComments(8, 0, 1000, 1).getCommentBOList().size());
        };

        for (int i = 0; i<10 ;i++) {
            create.run();
            get.run();
        }
    }


}
