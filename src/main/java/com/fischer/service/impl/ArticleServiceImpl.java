package com.fischer.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fischer.mapper.ArticleMapper;
import com.fischer.mapper.CommentMapper;
import com.fischer.mapper.FavoriteMapper;
import com.fischer.mapper.UserMapper;
import com.fischer.pojo.*;
import com.fischer.service.ArticleService;
import com.fischer.exception.BizException;
import com.fischer.exception.ExceptionStatus;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

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
public class ArticleServiceImpl implements ArticleService {
    private UserMapper userMapper;
    private ArticleMapper articleMapper;
    private FavoriteMapper favoriteMapper;
    private CommentMapper commentMapper;
    private final Integer LIMIT_LEVEL = 2;
    @Autowired
    ArticleServiceImpl (UserMapper userMapper,
                        ArticleMapper articleMapper,
                        FavoriteMapper favoriteMapper,
                        CommentMapper commentMapper){
        this.articleMapper = articleMapper;
        this.userMapper = userMapper;
        this.favoriteMapper = favoriteMapper;
        this.commentMapper = commentMapper;
    }

    @Override
    public Optional<ArticleDO> createArticle(String title, String description, String body, Integer type, Integer userId) {
        ArticleDO articleDO = new ArticleDO(title,body,description,type,userId);
        int insert = articleMapper.insert(articleDO);
        if(insert > 0) {
            log.info("创建文章成功，用户id为："+userId.toString());
            return Optional.of(articleDO);
        } else {
            log.warn("文章创建失败");
            return Optional.empty();
        }

    }

    @Override
    public Optional<ArticleBO> getArticleById(Integer articleId, Integer userId) {

        // 查询出文章和文章作者的详情
        ArticleDO articleDO = articleMapper.selectById(articleId);

        if(Objects.isNull(articleDO)) {
            log.error("获取文章详情时未获取到对应的文章id");
            throw new BizException(ExceptionStatus.INTERNAL_SERVER_ERROR);
        }
        // 补充匿名访问和点赞的相关信息
        ArticleBO articleBO = fillExtraInfo(articleDO, userId);
        return Optional.of(articleBO);

    }

    @Override
    public synchronized ArticleVO getArticles(Integer favoriteBy, Integer author, Integer type, Integer offset, Integer limit, Integer orderBy, Integer orderType, Integer userId) {
        MyPage myPage = new MyPage(offset,limit);
        List<ArticleDO> articleDOList = articleMapper.getArticles(favoriteBy, author, type, myPage, orderBy, orderType);
        // 补全用户相关信息
        List<ArticleBO> articleBOList = articleDOList.stream()
                .map(articleDO -> fillExtraInfo(articleDO, userId))
                .collect(Collectors.toList());
        Integer integer = articleMapper.selectArticleCount(favoriteBy,author,type);
        ArticleVO articleVO = new ArticleVO(articleBOList,integer);
        // 保证 list和总共数量的原子性 添加synchronized关键字
        return articleVO;
    }
    @Transactional(rollbackFor = {SQLException.class})
    @Override
    public Optional<ArticleBO> deleteArticle(Integer articleId, Integer userId) {
        ArticleDO articleDO = articleMapper.selectById(articleId);
        if(Objects.isNull(articleDO)) {
            log.error("当前要删除的文章不存在，要删除的文章id:"+articleId.toString()+"当前用户为:"+userId.toString());
            throw new BizException(ExceptionStatus.NOT_FOUND);
        }
        if(!Objects.equals(articleDO.getUserId(), userId)) {
            log.info("无权限操作，要删除的文章id:"+articleId.toString()+"当前用户为:"+userId.toString());
            throw new BizException(ExceptionStatus.FORBIDDEN);
        }
        int i = articleMapper.deleteById(articleId);
        if(i > 0){
            ArticleBO articleBO = fillExtraInfo(articleDO, userId);
            LambdaQueryWrapper<CommentDO> lqw = new LambdaQueryWrapper<>();
            lqw.eq(CommentDO::getArticleId,articleId);
            commentMapper.delete(lqw);
            return Optional.of(articleBO);
        } else {
            // 手动回滚 保证文章删除和相关评论删除的原子性
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            throw new BizException(ExceptionStatus.INTERNAL_SERVER_ERROR);

        }


    }

    @Override
    public synchronized ArticleVO getArticleFuzzy(String title, Integer userId,Integer offset,Integer limit) {
        MyPage myPage = new MyPage(offset,limit);
        IPage<ArticleDO> page = new Page(myPage.getOffset(),myPage.getLimit());

        LambdaQueryWrapper<ArticleDO> lqw = new LambdaQueryWrapper();
        lqw.like(Strings.isNotEmpty(title),ArticleDO::getTitle,title);
        // LIMIT_LEVEL为屏蔽级别 2级为屏蔽级别不予显示
        lqw.ne(ArticleDO::getStatus,LIMIT_LEVEL);
        IPage<ArticleDO> pageResult = articleMapper.selectPage(page, lqw);
        List<ArticleDO> articleDOList = pageResult.getRecords();
        List<ArticleBO> articleBOList = articleDOList.stream()
                .map(articleDO -> fillExtraInfo(articleDO, userId))
                .collect(Collectors.toList());
        Integer integer = articleMapper.selectCount(lqw);
        // synchronized 保证文章list和总数的原子性
        ArticleVO articleVO = new ArticleVO(articleBOList,integer);
        return articleVO;
    }
    @Transactional(rollbackFor = {SQLException.class})
    @Override
    public synchronized Optional<ArticleBO> favoriteArticle(Integer articleId, Integer userId) {
        LambdaQueryWrapper<FavoriteDO> lqw = new LambdaQueryWrapper<>();
        lqw.eq(FavoriteDO::getArticleId,articleId);
        lqw.eq(FavoriteDO::getUserId,userId);
        FavoriteDO favoriteDO = favoriteMapper.selectOne(lqw);
        if(!Objects.isNull(favoriteDO)){
            /*异常处理或者进行判断,*/
            // 无需回滚
            // TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            throw new BizException(403,"已经为点赞状态");
        } else{
            // 保证favorite和article的原子性
            // 点赞后更新热度
            FavoriteDO newFavorite = new FavoriteDO(articleId,userId);
            favoriteMapper.insert(newFavorite);
            ArticleDO articleDO = articleMapper.selectById(articleId);
            articleDO.setHeat(articleDO.getHeat()+1);

            articleMapper.updateById(articleDO);
            ArticleBO articleBO = fillExtraInfo(articleDO, userId);

            return Optional.of(articleBO);
        }

    }
    @Transactional(rollbackFor = {SQLException.class})
    @Override
    public synchronized Optional<ArticleBO> unfavoriteArticle(Integer articleId, Integer userId) {
        LambdaQueryWrapper<FavoriteDO> lqw = new LambdaQueryWrapper<>();
        lqw.eq(FavoriteDO::getArticleId,articleId);
        lqw.eq(FavoriteDO::getUserId,userId);
        FavoriteDO favoriteDO = favoriteMapper.selectOne(lqw);
        if(Objects.isNull(favoriteDO)){
            /*异常处理，后续再商议*/
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            throw new BizException(403,"已经为取消点赞状态");
        } else{
            favoriteMapper.delete(lqw);
            ArticleDO articleDO = articleMapper.selectById(articleId);
            ArticleBO articleBO = fillExtraInfo(articleDO, userId);
            articleDO.setHeat(articleDO.getHeat()-1);
            articleMapper.updateById(articleDO);
            return Optional.of(articleBO);
        }

    }

    Boolean isFavorite(Integer articleId,Integer userId){
        LambdaQueryWrapper<FavoriteDO> lqw = new LambdaQueryWrapper();
        lqw.eq(FavoriteDO::getArticleId,articleId);
        lqw.eq(FavoriteDO::getUserId,userId);
        FavoriteDO favoriteDO = favoriteMapper.selectOne(lqw);
        if(Objects.isNull(favoriteDO)) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * @param articleDO 查询出的文章;
     * @param userId 当前正在操作的用户，可能为空，代表匿名访问
    * */
    ArticleBO fillExtraInfo(ArticleDO articleDO, Integer userId) {

        UserDO userDO = userMapper.selectById(articleDO.getUserId());
        Boolean isFavorited = false;
        // 判断是否为匿名访问
        if(!Objects.isNull(userId)){
            isFavorited = isFavorite(articleDO.getId(),userId);
        }
        ArticleBO articleBO = new ArticleBO(articleDO,userDO,isFavorited);
        return articleBO;
    }

}
