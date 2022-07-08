package com.fischer.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * @author fischer
 */
@Data
@NoArgsConstructor
@TableName("articles")
public class ArticleDO {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String title;
    private String body;
    private String description;
    private String createdAt;
    private Integer type;
    private Integer status;
    private Integer heat;
    private Integer userId;

    public ArticleDO(String title, String body, String description, Integer type, Integer userId) {
        this.title = title;
        this.body = body;
        this.description = description;
        this.type = type;
        this.userId = userId;
    }
}
