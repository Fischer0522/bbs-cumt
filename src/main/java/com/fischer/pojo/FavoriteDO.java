package com.fischer.pojo;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


/**
 * @author fischer
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("favorite")
public class FavoriteDO implements Serializable {
    private Long articleId;
    private Long userId;
}
