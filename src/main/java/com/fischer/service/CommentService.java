package com.fischer.service;

import com.fischer.pojo.CommentBO;
import com.fischer.pojo.CommentReplyBO;
import com.fischer.pojo.CommentReplyDO;
import com.fischer.pojo.CommentVO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
/**
 * @author fischer
 */
@Service
public interface CommentService {
    /** 向指定文章添加评论
     * @param articleId 文章
     * @param body 评论内容
     * @param userId 当前用户
     * @return Optional+BO
     * */
    Optional<CommentBO> createComment(Long articleId, String body, Long userId);
    /** 删除评论
     * @param commentId 评论Id
     * @param userId 当前用户
     * @return Optional+BO*/
    Optional<CommentBO> deleteComment(Long commentId, Long userId);

    Optional<CommentReplyBO> createCommentReply(Long commentId, Long toId,  String content,Long userId);

    Optional<CommentReplyBO> deleteCommentReply(Long id,Long userId);

    Optional<CommentReplyBO> favoriteCommentReply(Long id,Long userId);

    Optional<CommentReplyBO> unfavoriteCommentReply(Long id,Long userId);


    Optional<CommentBO> favoriteComment(Long commentId,Long userId);

    Optional<CommentBO> unfavoriteComment(Long commentId,Long userId);
    /** 分页查询当前文章的评论
     * @param articleId 要查询的文章
     * @param offset 分页偏移量
     * @param limit 查询条数
     * @param orderType 排序方式 0为升序 1为降序 按发布时间进行排序
     * @return 返回List+数量*/
    CommentVO getComments(Long articleId, Integer offset, Integer limit, Integer orderType,Long userId);

    void deleteCommentFavorite(Long commentId);









}
