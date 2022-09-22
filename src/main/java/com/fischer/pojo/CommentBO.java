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
public class CommentBO {
    private String id;
    private String body;
    private String createAt;
    private Long articleId;
    private Integer favoriteCount;
    private Boolean favorite;
    @JsonProperty(value = "author")
    private UserVO userVO;


    public CommentBO(CommentDO commentDO, UserVO userVO){
        this.id = commentDO.getId().toString();
        this.body = commentDO.getBody();
        this.createAt =commentDO.getCreatedAt();
        this.articleId =commentDO.getArticleId();
        this.userVO = userVO;
    }

    public CommentBO(CommentDO commentDO, UserVO userVO,Integer favoriteCount,Boolean favorite){
        this.id = commentDO.getId().toString();
        this.body = commentDO.getBody();
        this.createAt =commentDO.getCreatedAt();
        this.articleId =commentDO.getArticleId();
        this.favoriteCount = favoriteCount;
        this.favorite = favorite;
        this.userVO = userVO;
    }
}
