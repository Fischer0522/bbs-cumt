package com.fischer.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@TableName("comments_reply")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentReplyDO implements Serializable {
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;
    private Long commentId;
    private Long fromId;
    private Long toId;
    private String content;
    private String createTime;
    private String updateTime;


    public CommentReplyDO (Long commentId,Long fromId,Long toId,String content) {
        this.commentId = commentId;
        this.fromId = fromId;
        this.toId = toId;
        this.content = content;
    }

}
