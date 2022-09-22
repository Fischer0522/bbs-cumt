package com.fischer.pojo;

import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


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
    private List<String> roles;

    public UserVO(UserDO userDO, List<String> roles) {
        this.id = userDO.getId().toString();
        this.email = userDO.getEmail();
        this.image = userDO.getImage();
        this.username = userDO.getUsername();
        this.roles = roles;
        this.token = null;
    }
    public UserVO(UserDO userDO, List<String> roles,String token) {
        this.id = userDO.getId().toString();
        this.email = userDO.getEmail();
        this.image = userDO.getImage();
        this.username = userDO.getUsername();
        this.roles = roles;
        this.token = token;
    }
}
