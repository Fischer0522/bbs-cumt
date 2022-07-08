package com.fischer.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fischer.pojo.ArticleDO;
import com.fischer.pojo.MyPage;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


/**
 * @author fisher
 */
@Mapper
public interface ArticleMapper extends BaseMapper<ArticleDO> {

    /**综合性查询,按条件查询并按不同字段进行排序
     * @param favoriteBy 被哪个用户点赞
     * @param author 筛选作者
     * @param  type 文章类型
     * @param myPage 自定义分页
     * @param orderBy 排序字段
     * @param orderType 排序顺序
     * @return 返回articleDO,之后进一步封装
     */

    List<ArticleDO> getArticles(
            @Param("favoriteBy") Integer favoriteBy,
            @Param("author") Integer author,
            @Param("type") Integer type,
            @Param("page")MyPage myPage,
            @Param("orderBy") Integer orderBy,
            @Param("orderType") Integer orderType
            );

    /**
     * 查询符合当前条件的文章总数
     * @param favoriteBy 按点赞筛选
     * @param  author 按作者筛选作者
     * @param  type 文章类型
     * @return 返回查询总数
     */

    Integer selectArticleCount(@Param("favoriteBy") Integer favoriteBy,
                               @Param("author") Integer author,
                               @Param("type") Integer type);
}
