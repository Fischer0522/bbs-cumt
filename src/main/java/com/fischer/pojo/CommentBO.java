package com.fischer.pojo;

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
    private UserDO userDO;


    public CommentBO(CommentDO commentDO, UserDO userDO){
        this.id = commentDO.getId().toString();
        this.body = commentDO.getBody();
        this.createAt =commentDO.getCreatedAt();
        this.articleId =commentDO.getArticleId();
        this.userDO = userDO;
    }

    public CommentBO(CommentDO commentDO, UserDO userDO,Integer favoriteCount,Boolean favorite){
        this.id = commentDO.getId().toString();
        this.body = commentDO.getBody();
        this.createAt =commentDO.getCreatedAt();
        this.articleId =commentDO.getArticleId();
        this.favoriteCount = favoriteCount;
        this.favorite = favorite;
        this.userDO = userDO;
    }
}
