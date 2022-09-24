package com.fischer.data;

import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.springframework.validation.annotation.Validated;

/**
 * @author fisher
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Validated
@JsonRootName("article")
public class UpdateArticleParam {
    @Length(max = 20,message = "文章标题最多20字")
    private String title;
    @Length(max = 200,message = "描述最多200字")
    private String description;
    @Length(max = 20000,message = "文章最大字数为20000字")
    private String content;
    private Integer type;
}
