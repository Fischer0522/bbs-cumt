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
@NoArgsConstructor
@ToString
@TableName("users")
public class UserDO implements Serializable {
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;
    private String username;
    private String email;
    private String image;
    public UserDO(String username,String email,String image){
        this.email = email;
        this.username = username;
        this.image = image;
    }
}
