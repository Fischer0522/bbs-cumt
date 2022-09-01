package com.fischer.data;

import com.fasterxml.jackson.annotation.JsonRootName;
import com.fischer.constraintValidator.DuplicatedArticleConstraint;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author fisher
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@JsonRootName("article")
@Validated
public class NewArticleParam {
    @NotBlank(message = "文章标题不能为空")
    @DuplicatedArticleConstraint
    private String title;
    @Length(max = 200,message = "描述最多200字")
    private String description;
    @Length(max = 20000,message = "文章最大字数为20000字")
    private String content;
    @NotNull(message = "请选择文章所属的板块")
    private Integer type;


}
