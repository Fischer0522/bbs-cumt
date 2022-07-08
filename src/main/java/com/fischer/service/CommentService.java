package com.fischer.service;

import com.fischer.pojo.CommentBO;
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
    Optional<CommentBO> createComment(Integer articleId, String body, Integer userId);
    /** 删除评论
     * @param commentId 评论Id
     * @param userId 当前用户
     * @return Optional+BO*/
    Optional<CommentBO> deleteComment(Integer commentId, Integer userId);
    /** 分页查询当前文章的评论
     * @param articleId 要查询的文章
     * @param offset 分页偏移量
     * @param limit 查询条数
     * @param orderType 排序方式 0为升序 1为降序 按发布时间进行排序
     * @return 返回List+数量*/
    CommentVO getComments(Integer articleId, Integer offset, Integer limit, Integer orderType);






}
