package com.fischer.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fischer.mapper.ArticleMapper;
import com.fischer.mapper.CommentMapper;
import com.fischer.mapper.UserMapper;
import com.fischer.pojo.*;
import com.fischer.service.CommentService;
import com.fischer.exception.BizException;
import com.fischer.exception.ExceptionStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;


/**
 * @author fischer
 */
@Service
@Slf4j
public class CommentServiceImpl implements CommentService {
    private CommentMapper commentMapper;
    private UserMapper userMapper;
    private ArticleMapper articleMapper;

    @Autowired
    public CommentServiceImpl(CommentMapper commentMapper,
                              UserMapper userMapper,
                              ArticleMapper articleMapper){
        this.articleMapper = articleMapper;
        this.userMapper = userMapper;
        this.commentMapper = commentMapper;
    }

    @Override
    public Optional<CommentBO> createComment(Integer articleId, String body, Integer userId) {
        ArticleDO articleDO = articleMapper.selectById(articleId);
        if(Objects.isNull(articleDO)) {
            log.warn("用户:"+userId.toString()+"添加评论失败");
            throw new BizException(404,"当前要评论的文章已不存在");
        }
        CommentDO commentDO = new CommentDO(body,articleId,userId);
        int insert = commentMapper.insert(commentDO);
        if(insert > 0) {
            CommentBO commentBO = fillExtraInfo(commentDO);
            log.info("用户:"+userId.toString()+"添加了评论");
            return Optional.of(commentBO);

        } else {
            log.warn("用户:"+userId.toString()+"添加评论失败");
            return Optional.empty();
        }

    }

    @Override
    public Optional<CommentBO> deleteComment(Integer commentId, Integer userId) {
        CommentDO commentDO = commentMapper.selectById(commentId);
        ArticleDO articleDO = articleMapper.selectById(commentDO.getArticleId());
        if (Objects.isNull(articleDO)) {
            log.error("当前评论无对应的文章,评论id:"+commentId);
            throw new BizException(ExceptionStatus.INTERNAL_SERVER_ERROR);
        }
        if (userId.equals(commentDO.getUserId())||userId.equals(articleDO.getUserId())) {
            commentMapper.deleteById(commentId);
            CommentBO commentBO = fillExtraInfo(commentDO);
            log.info("删除评论成功,用户id:"+userId.toString(),"评论id:"+commentId.toString());
            return Optional.of(commentBO);
        } else {
            log.warn("无权限删除评论,用户id"+userId.toString()+"评论id"+commentId.toString());
            throw new BizException(ExceptionStatus.FORBIDDEN);

        }

    }

    @Override
    public synchronized CommentVO getComments(Integer articleId, Integer offset, Integer limit, Integer orderType) {
        MyPage myPage = new MyPage(offset,limit);
        List<CommentDO> comments = commentMapper.getComments(orderType, articleId, myPage);
        List<CommentBO> commentBOList = comments.stream()
                .map(this::fillExtraInfo)
                .collect(Collectors.toList());
        LambdaQueryWrapper<CommentDO> lqw = new LambdaQueryWrapper<>();
        lqw.eq(CommentDO::getArticleId,articleId);
        Integer integer = commentMapper.selectCount(lqw);
        CommentVO commentVO = new CommentVO(commentBOList,integer);
        // 保证list和count查询的原子性
        return commentVO;

    }


    CommentBO fillExtraInfo(CommentDO commentDO) {
        Integer userId = commentDO.getUserId();
        UserDO userDO = userMapper.selectById(userId);
        CommentBO commentBO = new CommentBO(commentDO,userDO);
        return commentBO;
    }

}
