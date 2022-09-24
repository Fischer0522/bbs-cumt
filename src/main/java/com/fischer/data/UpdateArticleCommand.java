package com.fischer.data;

import com.fischer.constraintValidator.UpdateArticleConstraint;
import com.fischer.pojo.ArticleDO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

/**
 * @author fisher
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Validated
@UpdateArticleConstraint
public class UpdateArticleCommand {
    private ArticleDO targetArticle;
    private UpdateArticleParam updateArticleParam;
}
