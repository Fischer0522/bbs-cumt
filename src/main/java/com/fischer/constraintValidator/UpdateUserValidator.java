package com.fischer.constraintValidator;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fischer.data.UpdateUserCommand;
import com.fischer.mapper.UserMapper;
import com.fischer.pojo.UserDO;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Objects;

public class UpdateUserValidator implements ConstraintValidator<UpdateUserConstraint, UpdateUserCommand> {
    @Autowired
    private UserMapper userMapper;
    @Override
    public boolean isValid(UpdateUserCommand updateUserCommand, ConstraintValidatorContext context) {
        String username = updateUserCommand.getUpdateUserParam().getUsername();
        UserDO targetUser = updateUserCommand.getTargetUser();
        LambdaQueryWrapper<UserDO> lqw = new LambdaQueryWrapper<>();
        lqw.eq(UserDO::getUsername,username);
        UserDO userDO = userMapper.selectOne(lqw);
        Boolean isUsernameValid = false;
        if (Objects.isNull(userDO)||targetUser.equals(userDO)) {
            isUsernameValid = true;
        }

        if (isUsernameValid) {
            return true;
        } else {
            return false;
        }


    }
}
