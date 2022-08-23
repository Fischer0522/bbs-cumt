package com.fischer.pojo;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;


/**
 * @author fischer
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@TableName("adj")
public class AdjDO implements Serializable {
    private Integer id;
    private String word;
    private Integer used;
}
