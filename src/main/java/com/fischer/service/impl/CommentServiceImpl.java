package com.fischer.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fischer.data.MyPage;
import com.fischer.mapper.ArticleMapper;
import com.fischer.mapper.CommentFavoriteMapper;
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
    private CommentFavoriteMapper commentFavoriteMapper;

    @Autowired
    public CommentServiceImpl(CommentMapper commentMapper,
                              UserMapper userMapper,
                              ArticleMapper articleMapper,
                              CommentFavoriteMapper commentFavoriteMapper){
        this.articleMapper = articleMapper;
        this.userMapper = userMapper;
        this.commentMapper = commentMapper;
        this.commentFavoriteMapper = commentFavoriteMapper;
    }

    @Override
    public Optional<CommentBO> createComment(Integer articleId, String body, Integer userId) {
        ArticleDO articleDO = articleMapper.selectById(articleId);
        if(Objects.isNull(articleDO)) {
            log.warn("用户:"+userId.toString()+"添加评论失败");
            throw new BizException(404,"当前要评论的文章已不存在");
        }

        // 添加评论后更新文章的热度
        Integer currentHeat = articleDO.getHeat();
        Integer newHeat = currentHeat + 1;
        articleDO.setHeat(newHeat);
        articleMapper.updateById(articleDO);
        CommentDO commentDO = new CommentDO(body,articleId,userId);
        int insert = commentMapper.insert(commentDO);
        if(insert > 0) {
            CommentBO commentBO = fillExtraInfo(commentDO,userId);
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
            CommentBO commentBO = fillExtraInfo(commentDO,userId);
            log.info("删除评论成功,用户id:"+ userId,"评论id:"+commentId.toString());
            return Optional.of(commentBO);
        } else {
            log.warn("无权限删除评论,用户id"+ userId +"评论id"+commentId.toString());
            throw new BizException(ExceptionStatus.FORBIDDEN);

        }

    }

    @Override
    public Optional<CommentBO> favoriteComment(Integer commentId, Integer userId) {

        LambdaQueryWrapper<CommentFavoriteDO> lqw = new LambdaQueryWrapper<>();
        lqw.eq(CommentFavoriteDO::getCommentId,commentId);
        lqw.eq(CommentFavoriteDO::getUserId,userId);
        CommentFavoriteDO commentFavoriteDO = commentFavoriteMapper.selectOne(lqw);
        if (!Objects.isNull(commentFavoriteDO)) {
            throw new BizException(403,"已经为点赞状态");
        } else {
            CommentFavoriteDO newFavorite = new CommentFavoriteDO(commentId,userId);
            int insert = commentFavoriteMapper.insert(newFavorite);
            if (insert <=0) {
                throw new BizException(ExceptionStatus.INTERNAL_SERVER_ERROR);
            }
            CommentDO commentDO = commentMapper.selectById(commentId);
            CommentBO commentBO = fillExtraInfo(commentDO,userId);
            return Optional.of(commentBO);

        }
    }

    @Override
    public Optional<CommentBO> unfavoriteComment(Integer commentId, Integer userId) {
        LambdaQueryWrapper<CommentFavoriteDO> lqw = new LambdaQueryWrapper<>();
        lqw.eq(CommentFavoriteDO::getCommentId,commentId);
        lqw.eq(CommentFavoriteDO::getUserId,userId);
        CommentFavoriteDO commentFavoriteDO = commentFavoriteMapper.selectOne(lqw);
        if (Objects.isNull(commentFavoriteDO)) {
            throw new BizException(403,"已经为取消点赞的状态");
        } else {
            commentFavoriteMapper.delete(lqw);
        }

        CommentDO commentDO = commentMapper.selectById(commentId);
        CommentBO commentBO = fillExtraInfo(commentDO,userId);
        return Optional.of(commentBO);
    }

    @Override
    public synchronized CommentVO getComments(Integer articleId, Integer offset, Integer limit, Integer orderType,Integer userId) {
        MyPage myPage = new MyPage(offset,limit);
        List<CommentDO> comments = commentMapper.getComments(orderType, articleId, myPage);
        List<CommentBO> commentBOList = comments.stream()
                .map(s->fillExtraInfo(s,userId))
                .collect(Collectors.toList());
        LambdaQueryWrapper<CommentDO> lqw = new LambdaQueryWrapper<>();
        lqw.eq(CommentDO::getArticleId,articleId);
        Integer integer = commentMapper.selectCount(lqw);
        CommentVO commentVO = new CommentVO(commentBOList,integer);
        // 保证list和count查询的原子性
        return commentVO;

    }


    CommentBO fillExtraInfo(CommentDO commentDO,Integer userId) {
        Integer commentId = commentDO.getId();
        UserDO userDO = userMapper.selectById(commentDO.getUserId());
        // 查询点赞数
        LambdaQueryWrapper<CommentFavoriteDO> lqw = new LambdaQueryWrapper<>();
        lqw.eq(CommentFavoriteDO::getCommentId,commentDO.getId());
        Integer favoriteCount = commentFavoriteMapper.selectCount(lqw);

        // 确认点赞关系
        Boolean favorite = true;
        if (Objects.isNull(userId)) {
            favorite = false;
        } else {
            LambdaQueryWrapper<CommentFavoriteDO> favoriteLQW = new LambdaQueryWrapper<>();
            favoriteLQW.eq(CommentFavoriteDO::getCommentId,commentId);
            favoriteLQW.eq(CommentFavoriteDO::getUserId,userId);
            CommentFavoriteDO commentFavoriteDO = commentFavoriteMapper.selectOne(favoriteLQW);
            if (Objects.isNull(commentFavoriteDO)) {
                favorite = false;
            }

        }

        return new CommentBO(commentDO,userDO,favoriteCount,favorite);
    }

}
