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
    private Integer id;
    private String body;
    private String createAt;
    private Integer articleId;
    private Integer favoriteCount;
    private UserDO userDO;


    public CommentBO(CommentDO commentDO, UserDO userDO){
        this.id = commentDO.getId();
        this.body = commentDO.getBody();
        this.createAt =commentDO.getCreatedAt();
        this.articleId =commentDO.getArticleId();
        this.userDO = userDO;
    }

    public CommentBO(CommentDO commentDO, UserDO userDO,Integer favoriteCount){
        this.id = commentDO.getId();
        this.body = commentDO.getBody();
        this.createAt =commentDO.getCreatedAt();
        this.articleId =commentDO.getArticleId();
        this.favoriteCount = favoriteCount;
        this.userDO = userDO;
    }
}
