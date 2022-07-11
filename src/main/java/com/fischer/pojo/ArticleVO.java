package com.fischer.pojo;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;


/**
 * @author fischer
 */
@NoArgsConstructor
@ToString
@Data
public class ArticleVO {

    private List<ArticleBO> articleList;
    private Integer articleCount;

    public ArticleVO (List<ArticleBO> articleList ,Integer articleCount){
        this.articleCount = articleCount;
        this.articleList = articleList;
    }
}
