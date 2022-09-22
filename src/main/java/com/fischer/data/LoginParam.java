package com.fischer.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;


import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * @author fisher
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonRootName("user")
@Validated
public class LoginParam {
    @Email(message = "请输入正确的邮箱格式")
    @NotBlank(message = "邮箱格式不能为空")
    // @Pattern(regexp = "\\w+@cumt\\.edu\\.cn",message = "请使用矿大邮箱进行登录")
    private String email;
    @NotBlank(message = "验证码不能为空")
    private String verifyCode;
}
