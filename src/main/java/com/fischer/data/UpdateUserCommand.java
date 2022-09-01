package com.fischer.data;

import com.fischer.constraintValidator.DuplicatedUsernameConstraint;
import com.fischer.constraintValidator.UpdateUserConstraint;
import com.fischer.pojo.UserDO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

/**
 * @author fisher
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Validated
@UpdateUserConstraint
public class UpdateUserCommand {
    private UserDO targetUser;
    private UpdateUserParam updateUserParam;
}
