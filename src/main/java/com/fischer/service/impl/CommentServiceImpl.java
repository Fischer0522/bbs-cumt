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
    private CommentReplyMapper commentReplyMapper;
    private CommentReplyFavoriteMapper commentReplyFavoriteMapper;
    private CommentService commentService;

    @Autowired
    public CommentServiceImpl(CommentMapper commentMapper,
                              UserMapper userMapper,
                              ArticleMapper articleMapper,
                              CommentFavoriteMapper commentFavoriteMapper,
                              RoleMapper roleMapper,
                              CommentReplyMapper commentReplyMapper,
                              CommentReplyFavoriteMapper commentReplyFavoriteMapper){
        this.articleMapper = articleMapper;
        this.userMapper = userMapper;
        this.commentMapper = commentMapper;
        this.commentFavoriteMapper = commentFavoriteMapper;
        this.roleMapper = roleMapper;
        this.commentReplyMapper = commentReplyMapper;
        this.commentReplyFavoriteMapper = commentReplyFavoriteMapper;
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
        // 清除comment的favorite关系，在此之中会一并清除子评论的favorite
        deleteCommentFavorite(commentId);
        // 清除子评论
        LambdaQueryWrapper<CommentReplyDO> lqwDelete = new LambdaQueryWrapper<>();
        lqwDelete.eq(CommentReplyDO::getCommentId,commentId);
        commentReplyMapper.delete(lqwDelete);
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
    public Optional<CommentReplyBO> createCommentReply(Long commentId, Long toId,  String content,Long userId) {
        CommentReplyDO commentReplyDO = new CommentReplyDO(commentId,userId,toId,content);
        int insert = commentReplyMapper.insert(commentReplyDO);
        if (insert > 0) {
            return Optional.of(fillExtraInfo(commentReplyDO,userId));
        }
        return Optional.empty();
    }

    @Override
    public Optional<CommentReplyBO> deleteCommentReply(Long id,Long userId) {
        CommentReplyDO commentReplyDO = commentReplyMapper.selectById(id);
        if (Objects.isNull(commentReplyDO)) {
            throw new BizException(ExceptionStatus.ERROR_NO_COMMENT_REPLY);
        }
        if (!userId.equals(commentReplyDO.getFromId())) {
            throw new BizException(ExceptionStatus.ERROR_NOT_AUTH);
        }

        int i = commentReplyMapper.deleteById(id);
        if (i > 0) {
            return Optional.of(fillExtraInfo(commentReplyDO,userId));
        }
        deleteReplyFavorite(id);
        return Optional.empty();
    }

    @Override
    public Optional<CommentReplyBO> favoriteCommentReply(Long id, Long userId) {
        LambdaQueryWrapper<CommentReplyFavoriteDO> lqw = new LambdaQueryWrapper<>();
        lqw.eq(CommentReplyFavoriteDO::getCommentReplyId,id);
        lqw.eq(CommentReplyFavoriteDO::getUserId,userId);
        CommentReplyFavoriteDO commentReplyFavoriteDO = commentReplyFavoriteMapper.selectOne(lqw);
        if (!Objects.isNull(commentReplyFavoriteDO)) {
            throw new BizException(ExceptionStatus.ERROR_LIKE);
        }
        CommentReplyFavoriteDO newFavorite = new CommentReplyFavoriteDO(id,userId);
        int insert = commentReplyFavoriteMapper.insert(newFavorite);
        if (insert <= 0) {
            throw  new BizException(ExceptionStatus.INTERNAL_SERVER_ERROR);
        }

        CommentReplyDO commentReplyDO = commentReplyMapper.selectById(id);

        return Optional.ofNullable(fillExtraInfo(commentReplyDO,userId));
    }

    @Override
    public Optional<CommentReplyBO> unfavoriteCommentReply(Long id, Long userId) {
        LambdaQueryWrapper<CommentReplyFavoriteDO> lqw = new LambdaQueryWrapper<>();
        lqw.eq(CommentReplyFavoriteDO::getCommentReplyId,id);
        lqw.eq(CommentReplyFavoriteDO::getUserId,userId);

        CommentReplyFavoriteDO commentReplyFavoriteDO = commentReplyFavoriteMapper.selectOne(lqw);
        if (Objects.isNull(commentReplyFavoriteDO)) {
            throw new BizException(ExceptionStatus.ERROR_DISLIKE);
        }
        LambdaQueryWrapper<CommentReplyFavoriteDO> lqwDelete = new LambdaQueryWrapper<>();
        lqwDelete.eq(CommentReplyFavoriteDO::getCommentReplyId,id);
        lqwDelete.eq(CommentReplyFavoriteDO::getUserId,userId);
        int i = commentReplyFavoriteMapper.delete(lqwDelete);

        CommentReplyDO commentReplyDO = commentReplyMapper.selectById(id);

        return Optional.ofNullable(fillExtraInfo(commentReplyDO,userId));
    }

    @Override
    public Optional<CommentBO> favoriteComment(Long commentId, Long userId) {

        LambdaQueryWrapper<CommentFavoriteDO> lqw = new LambdaQueryWrapper<>();
        lqw.eq(CommentFavoriteDO::getCommentId,commentId);
        lqw.eq(CommentFavoriteDO::getUserId,userId);
        CommentFavoriteDO commentFavoriteDO = commentFavoriteMapper.selectOne(lqw);
        if (!Objects.isNull(commentFavoriteDO)) {
            throw new BizException(ExceptionStatus.ERROR_LIKE);
        }
        CommentFavoriteDO newFavorite = new CommentFavoriteDO(commentId,userId);
        int insert = commentFavoriteMapper.insert(newFavorite);
        if (insert <=0) {
            throw new BizException(ExceptionStatus.INTERNAL_SERVER_ERROR);
        }
        CommentDO commentDO = commentMapper.selectById(commentId);
        CommentBO commentBO = fillExtraInfo(commentDO,userId);
        return Optional.of(commentBO);

    }

    @Override
    public Optional<CommentBO> unfavoriteComment(Long commentId, Long userId) {
        LambdaQueryWrapper<CommentFavoriteDO> lqw = new LambdaQueryWrapper<>();
        lqw.eq(CommentFavoriteDO::getCommentId,commentId);
        lqw.eq(CommentFavoriteDO::getUserId,userId);
        CommentFavoriteDO commentFavoriteDO = commentFavoriteMapper.selectOne(lqw);
        if (Objects.isNull(commentFavoriteDO)) {
            throw new BizException(ExceptionStatus.ERROR_DISLIKE);
        } else {
            commentFavoriteMapper.delete(lqw);
        }

        CommentDO commentDO = commentMapper.selectById(commentId);
        CommentBO commentBO = fillExtraInfo(commentDO,userId);
        return Optional.of(commentBO);
    }
    @Transactional(rollbackFor = SQLException.class)
    @Override
    public  CommentVO getComments(Long articleId, Integer offset, Integer limit, Integer orderType,Long userId) {
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
        lqwRoles.eq(RoleDO::getUserId,userId);
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
        /*查询对应的子评论*/
        LambdaQueryWrapper<CommentReplyDO> lqwCommentReply = new LambdaQueryWrapper<>();
        lqwCommentReply.eq(CommentReplyDO::getCommentId,commentDO.getId());
        List<CommentReplyBO> commentReplyList = commentReplyMapper.selectList(lqwCommentReply).stream().map(s -> fillExtraInfo(s, userId)).collect(Collectors.toList());

        return new CommentBO(commentDO,userVO,favoriteCount,favorite,commentReplyList);

    }

    CommentReplyBO fillExtraInfo(CommentReplyDO commentReplyDO,Long userId) {
        // 获取当前子评论的id和回复的人以及被回复的人
        Long commentReplyId = commentReplyDO.getId();
        UserDO fromUser = userMapper.selectById(commentReplyDO.getFromId());
        UserDO toUser = userMapper.selectById(commentReplyDO.getToId());
        // 补充用户信息
        LambdaQueryWrapper<RoleDO> lambdaQueryWrapperRolesTo = new LambdaQueryWrapper<>();
        lambdaQueryWrapperRolesTo.eq(RoleDO::getUserId,toUser.getId());
        List<String> toRoles = roleMapper.selectList(lambdaQueryWrapperRolesTo).stream().map(s -> s.getRole()).collect(Collectors.toList());
        LambdaQueryWrapper<RoleDO> lambdaQueryWrapperRolesFrom = new LambdaQueryWrapper<>();
        lambdaQueryWrapperRolesFrom.eq(RoleDO::getUserId,fromUser.getId());
        List<String> fromRoles = roleMapper.selectList(lambdaQueryWrapperRolesFrom).stream().map(s -> s.getRole()).collect(Collectors.toList());
        UserVO fromUserVO = new UserVO(fromUser,fromRoles);
        UserVO toUserVO = new UserVO(toUser,toRoles);

        // 补充favorite信息
        Boolean favorite = true;
        if (Objects.isNull(userId)) {
            favorite = false;
        } else {
            LambdaQueryWrapper<CommentReplyFavoriteDO> lqwFavorite = new LambdaQueryWrapper<>();
            lqwFavorite.eq(CommentReplyFavoriteDO::getCommentReplyId,commentReplyId);
            lqwFavorite.eq(CommentReplyFavoriteDO::getUserId,userId);
            CommentReplyFavoriteDO commentReplyFavoriteDO = commentReplyFavoriteMapper.selectOne(lqwFavorite);
            if (Objects.isNull(commentReplyFavoriteDO)) {
                favorite = false;
            }


        }
        LambdaQueryWrapper<CommentReplyFavoriteDO> lqwFavoriteCount = new LambdaQueryWrapper<>();
        lqwFavoriteCount.eq(CommentReplyFavoriteDO::getCommentReplyId,commentReplyId);
        Integer favoriteCount = commentReplyFavoriteMapper.selectCount(lqwFavoriteCount);

        return new CommentReplyBO(commentReplyDO,fromUserVO,toUserVO,favorite,favoriteCount);

    }
    @Transactional(rollbackFor = SQLException.class)
    public void deleteCommentFavorite(Long commentId) {
        LambdaQueryWrapper<CommentFavoriteDO> lqw = new LambdaQueryWrapper<>();
        lqw.eq(CommentFavoriteDO::getCommentId,commentId);
        int delete = commentFavoriteMapper.delete(lqw);

        LambdaQueryWrapper<CommentReplyDO> lqwSelect = new LambdaQueryWrapper<>();
        lqwSelect.eq(CommentReplyDO::getCommentId,commentId);
        List<CommentReplyDO> commentReplyDOS = commentReplyMapper.selectList(lqwSelect);
        for (CommentReplyDO com: commentReplyDOS
             ) {
            deleteReplyFavorite(com.getId());
        }

    }

    private void deleteReplyFavorite(Long commentReplyId) {
        LambdaQueryWrapper<CommentReplyFavoriteDO> lqw = new LambdaQueryWrapper<>();
        lqw.eq(CommentReplyFavoriteDO::getCommentReplyId,commentReplyId);
        int delete = commentReplyFavoriteMapper.delete(lqw);

    }



}
