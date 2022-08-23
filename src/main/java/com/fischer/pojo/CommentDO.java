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
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String body;
    private Integer articleId;
    private Integer userId;
    private String createdAt;

    public CommentDO(String body,Integer articleId,Integer userId){
        this.body = body;
        this.articleId = articleId;
        this.userId = userId;
    }
}
