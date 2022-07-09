package com.fischer.service;

import com.fischer.pojo.ArticleDO;
import com.fischer.pojo.ArticleBO;
import com.fischer.pojo.ArticleVO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
/**进一步问封装，对文章进行CRUD，返回的形式直接为最终交给前端的诗数据形式
 * @author fischer
 */
@Service
public interface ArticleService {
     /**填写文章基本信息，创建文章
      * @param title 文章标题
      * @param description 文章描述
      * @param body 文章主体
      * @param type 文章类型,所属板块
      * @param userId 当前用户的Id
      * @return 返回Optional类型
      *
      **/

     Optional<ArticleDO> createArticle(String title, String description, String body, Integer type, Integer userId);

     /**按照条件查询相应的文章，相比Dao层添加了当前用户是否已经点赞，以及文章总数
      * @return ArticleVO 用于给前端
      * @param favoriteBy 被谁点赞
      * @param author 按作者筛选
      * @param type 文章所属板块
      * @param offset 偏移量
      * @param limit 查询长度
      * @param orderBy 排序字段，0为按时间，1为按热度
      * @param orderType 排序顺序，0为升序，1为降序
      * @param userId 当前用户
      * */
     ArticleVO getArticles(Integer favoriteBy,
                           Integer author,
                           Integer type,
                           Integer offset,
                           Integer limit,
                           Integer orderBy,
                           Integer orderType,
                           Integer userId);

     /** 删除文章，鉴权
      * @param articleId 要删除的文章Id
      * @param userId 当前操作的用户
      * @return Optional类型BO
      */
     Optional<ArticleBO> deleteArticle(Integer articleId, Integer userId);

     /** 模糊查询文章标题
      * @param title 用于模糊查询文章标题
      * @param userId 当前操作的用户
      * @return 返回VO
      */

     ArticleVO getArticleFuzzy(String title, Integer userId,Integer offset,Integer limit);

     /** 点赞文章，不可重复点赞
      * @param articleId 要点赞的文章Id
      * @param userId 当前用户
      * @return Optional+BO
      * */

     Optional<ArticleBO> favoriteArticle(Integer articleId, Integer userId);

     /** 取消点赞
      * @param articleId 取消点赞的文章
      * @param userId 当前用户
      * @return Optional+BO
      */
     Optional<ArticleBO> unfavoriteArticle(Integer articleId, Integer userId);






}
