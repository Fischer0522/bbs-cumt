package com.fischer.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fischer.data.MyPage;
import com.fischer.mapper.*;
import com.fischer.pojo.*;
import com.fischer.service.CommentService;
import com.fischer.exception.BizException;
import com.fischer.exception.ExceptionStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
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
    private RoleMapper roleMapper;

    @Autowired
    public CommentServiceImpl(CommentMapper commentMapper,
                              UserMapper userMapper,
                              ArticleMapper articleMapper,
                              CommentFavoriteMapper commentFavoriteMapper,
                              RoleMapper roleMapper){
        this.articleMapper = articleMapper;
        this.userMapper = userMapper;
        this.commentMapper = commentMapper;
        this.commentFavoriteMapper = commentFavoriteMapper;
        this.roleMapper = roleMapper;
    }

    @Override
    public Optional<CommentBO> createComment(Long articleId, String body, Long userId) {
        ArticleDO articleDO = articleMapper.selectById(articleId);
        if(Objects.isNull(articleDO)) {
            log.warn("用户:"+userId.toString()+"添加评论失败");
            throw new BizException(ExceptionStatus.ERROR_GET_ARTICLE_FAIL);
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
    @Transactional(rollbackFor = {SQLException.class})
    public Optional<CommentBO> deleteComment(Long commentId, Long userId) {
        CommentDO commentDO = commentMapper.selectById(commentId);
        ArticleDO articleDO = articleMapper.selectById(commentDO.getArticleId());

        if (Objects.isNull(articleDO)) {
            log.error("当前评论无对应的文章,评论id:"+commentId);
            throw new BizException(ExceptionStatus.INTERNAL_SERVER_ERROR);
        }
        if (userId.equals(commentDO.getUserId())||userId.equals(articleDO.getUserId())) {
            int i = commentMapper.deleteById(commentId);
            if (i > 0) {
                CommentBO commentBO = fillExtraInfo(commentDO,userId);
                log.info("删除评论成功,用户id:"+ userId,"评论id:"+commentId.toString());
                // 清除 被删除评论的favorite信息
                LambdaQueryWrapper<CommentFavoriteDO> favoriteDOLambdaQueryWrapper = new LambdaQueryWrapper<>();
                favoriteDOLambdaQueryWrapper.eq(CommentFavoriteDO::getCommentId,commentId);
                commentFavoriteMapper.delete(favoriteDOLambdaQueryWrapper);

                return Optional.of(commentBO);
            } else {
                throw new BizException(ExceptionStatus.INTERNAL_SERVER_ERROR);
            }

        } else {
            log.warn("无权限删除评论,用户id"+ userId +"评论id"+commentId.toString());
            throw new BizException(ExceptionStatus.ERROR_NOT_AUTH);

        }

    }

    @Override
    public Optional<CommentBO> favoriteComment(Long commentId, Long userId) {

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
    public Optional<CommentBO> unfavoriteComment(Long commentId, Long userId) {
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
    public synchronized CommentVO getComments(Long articleId, Integer offset, Integer limit, Integer orderType,Long userId) {
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


    CommentBO fillExtraInfo(CommentDO commentDO,Long userId) {
        Long commentId = commentDO.getId();
        UserDO userDO = userMapper.selectById(commentDO.getUserId());
        LambdaQueryWrapper<RoleDO> lqwRoles = new LambdaQueryWrapper<>();
        List<String> roles = roleMapper.selectList(lqwRoles).stream().map(s -> s.getRole()).collect(Collectors.toList());
        UserVO userVO = new UserVO(userDO,roles);
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

        return new CommentBO(commentDO,userVO,favoriteCount,favorite);
    }

}
