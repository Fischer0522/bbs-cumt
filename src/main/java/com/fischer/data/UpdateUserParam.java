package com.fischer.data;

import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Pattern;

/**
 * @author fisher
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonRootName("user")
@Validated
public class UpdateUserParam {
    @Pattern(regexp = "[\\u4e00-\\u9fa5_a-zA-Z0-9-]{1,20}",message ="用户名只支持20位以内的中英文和数字以及下划线" )
    String username;

    String image;
}
