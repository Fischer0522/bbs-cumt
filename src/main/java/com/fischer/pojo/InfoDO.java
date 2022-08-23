package com.fischer.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
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
@TableName("info")
public class InfoDO implements Serializable {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String randName;
    private String randImage;
    private Integer used;
}
