package com.fischer.pojo;

import com.fasterxml.jackson.annotation.JsonRootName;
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
@JsonRootName("articles")
public class ArticleVO {

    private List<ArticleBO> articleList;
    private Integer articleCount;

    public ArticleVO (List<ArticleBO> articleList ,Integer articleCount){
        this.articleCount = articleCount;
        this.articleList = articleList;
    }
}
