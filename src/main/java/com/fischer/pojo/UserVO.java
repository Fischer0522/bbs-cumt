package com.fischer.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * @author fischer
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserVO {
    private Integer id;
    private String username;
    private String email;
    private String image;
    private String token;

    public UserVO(UserDO userDO,String token) {
        this.id = userDO.getId();
        this.email = userDO.getEmail();
        this.image = userDO.getImage();
        this.username = userDO.getUsername();
        this.token = token;
    }
}
