package com.fischer.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CommentReplyBO {

    private String id;
    private String commentId;
    private String content;
    private String createTime;
    private String updateTime;
    private UserVO fromUser;
    private UserVO toUser;
    private Boolean favorite;
    private Integer favoriteCount;

    public CommentReplyBO(CommentReplyDO commentReplyDO,UserVO fromUser,UserVO toUser,Boolean favorite,Integer favoriteCount) {
        this.id = commentReplyDO.getId().toString();
        this.commentId = commentReplyDO.getCommentId().toString();
        this.content = commentReplyDO.getContent();
        this.createTime = commentReplyDO.getCreateTime();
        this.updateTime = commentReplyDO.getUpdateTime();
        this.fromUser = fromUser;
        this.toUser = toUser;
        this.favorite = favorite;
        this.favoriteCount = favoriteCount;
    }

}
