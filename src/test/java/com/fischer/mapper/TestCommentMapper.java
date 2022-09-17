package com.fischer.mapper;

import com.fischer.pojo.CommentDO;
import com.fischer.data.MyPage;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class TestCommentMapper {
    private CommentMapper commentMapper;
    @Autowired
    public TestCommentMapper (CommentMapper commentMapper){
        this.commentMapper = commentMapper;
    }


}
