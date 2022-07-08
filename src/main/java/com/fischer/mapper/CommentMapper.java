package com.fischer.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fischer.pojo.CommentDO;
import com.fischer.pojo.MyPage;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author fisher
 */
@Mapper
public interface CommentMapper extends BaseMapper<CommentDO> {

    /** 按照文章的Id查询对应的评论
     * @return 返回DO原始形式的List集合，在Service层中进一步封装
     * @param orderType 排序方式，0为升序，1为降序
     * @param articleId 评论对应的文章id
     * @param myPage 自定义分页器
     *
     */

    public List<CommentDO> getComments(
            @Param("orderType") Integer orderType,
            @Param("articleId") Integer articleId,
            @Param("page")MyPage myPage
            );
}
