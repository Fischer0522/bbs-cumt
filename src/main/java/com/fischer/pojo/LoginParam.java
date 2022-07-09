package com.fischer.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author fisher
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginParam {
    private String email;
    private String verifyCode;
}
