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
    private String id;
    private String title;
    private String body;
    private String description;
    private String createAt;
    private Integer type;
    private Integer status;
    private Integer heat;
    private Boolean favorited;
    private Integer favoriteCount;
    private String image;
    @JsonProperty(value = "author")
    private UserVO userVO;


    public ArticleBO(ArticleDO articleDO, UserVO userVO, Boolean favorited,Integer favoriteCount){
        this.id = articleDO.getId().toString();
        this.title = articleDO.getTitle();
        this.body = articleDO.getBody();
        this.description = articleDO.getDescription();
        this.createAt = articleDO.getCreatedAt();
        this.type = articleDO.getType();
        this.status = articleDO.getStatus();
        this.heat = articleDO.getHeat();
        this.favorited =favorited;
        this.favoriteCount = favoriteCount;
        this.userVO = userVO;
        this.image = articleDO.getImage();
    }
}
