package com.fischer.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fischer.mapper.ArticleMapper;
import com.fischer.mapper.CommentMapper;
import com.fischer.mapper.FavoriteMapper;
import com.fischer.mapper.UserMapper;
import com.fischer.pojo.*;
import com.fischer.service.ArticleService;
import com.fischer.exception.BizException;
import com.fischer.exception.ExceptionStatus;
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
public class ArticleServiceImpl implements ArticleService {
    private UserMapper userMapper;
    private ArticleMapper articleMapper;
    private FavoriteMapper favoriteMapper;
    private CommentMapper commentMapper;
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
            return Optional.of(articleDO);
        } else {
            return Optional.empty();
        }

    }

    @Override
    public ArticleVO getArticles(Integer favoriteBy, Integer author, Integer type, Integer offset, Integer limit, Integer orderBy, Integer orderType, Integer userId) {
        MyPage myPage = new MyPage(offset,limit);
        List<ArticleDO> articleDOList = articleMapper.getArticles(favoriteBy, author, type, myPage, orderBy, orderType);
        List<ArticleBO> articleBOList = articleDOList.stream()
                .map(articleDO -> fillExtraInfo(articleDO, userId))
                .collect(Collectors.toList());
        Integer integer = articleMapper.selectArticleCount(favoriteBy,author,type);
        ArticleVO articleVO = new ArticleVO(articleBOList,integer);
        /*多线程问题尚待解决*/
        return articleVO;
    }
    @Transactional(rollbackFor = {SQLException.class})
    @Override
    public Optional<ArticleBO> deleteArticle(Integer articleId, Integer userId) {
        ArticleDO articleDO = articleMapper.selectById(articleId);
        if(Objects.isNull(articleDO)) {
            throw new BizException(ExceptionStatus.NOT_FOUND);
        }
        if(!Objects.equals(articleDO.getUserId(), userId)) {
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
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            throw new BizException(ExceptionStatus.INTERNAL_SERVER_ERROR);

        }


    }

    @Override
    public ArticleVO getArticleFuzzy(String title, Integer userId) {
        LambdaQueryWrapper<ArticleDO> lqw = new LambdaQueryWrapper();
        lqw.like(Strings.isNotEmpty(title),ArticleDO::getTitle,title);
        lqw.ne(ArticleDO::getStatus,2);
        List<ArticleDO> articleDOList = articleMapper.selectList(lqw);
        List<ArticleBO> articleBOList = articleDOList.stream()
                .map(articleDO -> fillExtraInfo(articleDO, userId))
                .collect(Collectors.toList());
        Integer integer = articleMapper.selectCount(lqw);
        /*多线程问题尚待解决*/
        ArticleVO articleVO = new ArticleVO(articleBOList,integer);
        return articleVO;
    }
    @Transactional(rollbackFor = {SQLException.class})
    @Override
    public Optional<ArticleBO> favoriteArticle(Integer articleId, Integer userId) {
        LambdaQueryWrapper<FavoriteDO> lqw = new LambdaQueryWrapper<>();
        lqw.eq(FavoriteDO::getArticleId,articleId);
        lqw.eq(FavoriteDO::getUserId,userId);
        FavoriteDO favoriteDO = favoriteMapper.selectOne(lqw);
        if(!Objects.isNull(favoriteDO)){
            /*异常处理或者进行判断,*/
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            throw new BizException(403,"已经为点赞状态");
        } else{
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
    public Optional<ArticleBO> unfavoriteArticle(Integer articleId, Integer userId) {
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


    ArticleBO fillExtraInfo(ArticleDO articleDO, Integer userId) {

        UserDO userDO = userMapper.selectById(articleDO.getUserId());
        Boolean isFavorited = false;
        if(!Objects.isNull(userId)){
            isFavorited = isFavorite(articleDO.getId(),userId);
        }
        ArticleBO articleBO = new ArticleBO(articleDO,userDO,isFavorited);
        return articleBO;
    }

}
