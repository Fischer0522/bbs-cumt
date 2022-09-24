package com.fischer.constraintValidator;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fischer.data.UpdateArticleCommand;
import com.fischer.mapper.ArticleMapper;
import com.fischer.pojo.ArticleDO;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Objects;

/**
 * @author fisher
 */
public class UpdateArticleValidator implements ConstraintValidator<UpdateArticleConstraint, UpdateArticleCommand> {

    @Autowired
    private ArticleMapper articleMapper;

    @Override
    public boolean isValid(UpdateArticleCommand updateArticleCommand, ConstraintValidatorContext constraintValidatorContext) {

        String title = updateArticleCommand.getUpdateArticleParam().getTitle();
        ArticleDO targetArticle = updateArticleCommand.getTargetArticle();
        LambdaQueryWrapper<ArticleDO> lqw = new LambdaQueryWrapper<>();
        lqw.eq(ArticleDO::getTitle,title);
        ArticleDO articleDO = articleMapper.selectOne(lqw);
        boolean isArticleValid = false;
        if (Objects.isNull(articleDO)||targetArticle.equals(articleDO)) {
            isArticleValid = true;

        }
        return  isArticleValid;

    }
}
