package com.fischer.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

/**
 * @author fisher
 */
@Data
@NoArgsConstructor
@ToString
public class CommentVO {
    private List<CommentBO> commentBOList;
    private Integer count;

    public CommentVO(List<CommentBO> commentBOList,Integer count){
        this.count = count;
        this.commentBOList = commentBOList;
    }


}
