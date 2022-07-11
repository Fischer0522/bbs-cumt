package com.fischer.constraintValidator;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fischer.mapper.ArticleMapper;
import com.fischer.pojo.ArticleDO;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Objects;

public class DuplicatedArticleValidator implements ConstraintValidator<DuplicatedArticleConstraint,String> {
    @Autowired
    private ArticleMapper articleMapper;
    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        LambdaQueryWrapper<ArticleDO> lqw = new LambdaQueryWrapper();
        lqw.eq(ArticleDO::getTitle,s);
        ArticleDO articleDO = articleMapper.selectOne(lqw);
        return Objects.isNull(articleDO);

    }
}
