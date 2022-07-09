package com.fischer.param;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.fischer.pojo.UserDO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @author fisher
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateUserCommand {
    private UserDO targetUser;
    private UpdateUserParam updateUserParam;
}
