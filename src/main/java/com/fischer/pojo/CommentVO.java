package com.fischer.pojo;

import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author fisher
 */
@Data
@NoArgsConstructor
@ToString
@JsonRootName("comments")
public class CommentVO {
    private List<CommentBO> commentBOList;
    private Integer count;

    public CommentVO(List<CommentBO> commentBOList,Integer count){
        this.count = count;
        this.commentBOList = commentBOList;
    }


}
