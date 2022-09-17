package com.fischer.pojo;

import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * @author fischer
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonRootName("user")
public class UserVO {
    private String id;
    private String username;
    private String email;
    private String image;
    private String token;

    public UserVO(UserDO userDO,String token) {
        this.id = userDO.getId().toString();
        this.email = userDO.getEmail();
        this.image = userDO.getImage();
        this.username = userDO.getUsername();
        this.token = token;
    }
}
