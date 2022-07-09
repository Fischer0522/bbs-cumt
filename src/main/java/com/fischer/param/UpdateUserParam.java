package com.fischer.param;

import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author fisher
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonRootName("user")
public class UpdateUserParam {

    String username;

    String image;
}
