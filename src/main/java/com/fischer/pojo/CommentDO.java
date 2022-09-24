package com.fischer.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


/**
 * @author fischer
 */
@Data
@NoArgsConstructor
@TableName("comments")
public class CommentDO implements Serializable {
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;
    private String body;
    private Long articleId;
    private Long userId;
    private String createdAt;
    private String updatedAt;

    public CommentDO(String body,Long articleId,Long userId){
        this.body = body;
        this.articleId = articleId;
        this.userId = userId;
    }
}
