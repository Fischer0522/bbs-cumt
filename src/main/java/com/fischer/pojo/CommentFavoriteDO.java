package com.fischer.pojo;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@TableName("comment_favorite")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentFavoriteDO {

    private Integer commentId;
    private Integer userId;
}
