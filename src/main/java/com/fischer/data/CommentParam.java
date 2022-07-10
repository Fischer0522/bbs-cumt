package com.fischer.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @author fisher
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@JsonRootName("comment")
public class CommentParam {
    @JsonProperty(value = "content")
    private String body;
    private Integer articleId;
}
