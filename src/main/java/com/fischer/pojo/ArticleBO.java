package com.fischer.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * @author fischer
 */
@Data
@NoArgsConstructor
public class ArticleBO {
    private Integer id;
    private String title;
    private String body;
    private String description;
    private String createAt;
    private Integer type;
    private Integer status;
    private Integer heat;
    private Boolean favorited;
    @JsonProperty(value = "author")
    private UserDO userDO;

    public ArticleBO(ArticleDO articleDO, UserDO userDO, Boolean favorited){
        this.id = articleDO.getId();
        this.title = articleDO.getTitle();
        this.body = articleDO.getBody();
        this.description = articleDO.getDescription();
        this.createAt = articleDO.getCreatedAt();
        this.type = articleDO.getType();
        this.status = articleDO.getStatus();
        this.heat = articleDO.getHeat();
        this.favorited =favorited;
        this.userDO = userDO;
    }
}