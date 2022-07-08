package com.fischer.pojo;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;


/**
 * @author fischer
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@TableName("adj")
public class AdjDO {
    private Integer id;
    private String word;
    private Integer used;
}
