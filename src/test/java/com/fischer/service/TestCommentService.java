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







}
