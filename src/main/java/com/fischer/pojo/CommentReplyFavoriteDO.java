package com.fischer.pojo;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("comments_reply_favorite")
public class CommentReplyFavoriteDO implements Serializable {
    private Long commentReplyId;
    private Long userId;
}
