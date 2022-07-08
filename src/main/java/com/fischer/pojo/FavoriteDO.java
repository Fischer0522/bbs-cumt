package com.fischer.pojo;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * @author fischer
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("favorite")
public class FavoriteDO {
    private Integer articleId;
    private Integer userId;
}
