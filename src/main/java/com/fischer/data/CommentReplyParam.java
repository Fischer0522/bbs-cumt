package com.fischer.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;

@Validated
@AllArgsConstructor
@NoArgsConstructor
@Data
@JsonRootName("commentReply")
public class CommentReplyParam {
    private String toUser;
    @JsonProperty(value = "content")
    @NotBlank(message = "真的不再写几个字吗")
    @Length(max = 300,message = "最大长度为300个字符")
    private String content;

}
