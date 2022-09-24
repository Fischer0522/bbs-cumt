package com.fischer.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


/**
 * @author fischer
 */
@Data
@NoArgsConstructor
public class CommentBO {
    private String id;
    private String body;
    private String createdAt;
    private String updatedAt;
    private Long articleId;
    private Integer favoriteCount;
    private Boolean favorite;
    @JsonProperty(value = "author")
    private UserVO userVO;
    private List<CommentReplyBO> commentReplyList;

    public CommentBO(CommentDO commentDO, UserVO userVO,Integer favoriteCount,Boolean favorite,List<CommentReplyBO> commentReplyList){
        this.id = commentDO.getId().toString();
        this.body = commentDO.getBody();
        this.createdAt =commentDO.getCreatedAt();
        this.updatedAt = commentDO.getUpdatedAt();
        this.articleId =commentDO.getArticleId();
        this.favoriteCount = favoriteCount;
        this.favorite = favorite;
        this.userVO = userVO;
        this.commentReplyList = commentReplyList;
    }
}
