package com.fischer.constraintValidator;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fischer.mapper.UserMapper;
import com.fischer.pojo.UserDO;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class DuplicatedUsernameValidator implements ConstraintValidator<DuplicatedUsernameConstraint,String> {


    @Autowired
    private UserMapper userMapper;

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        LambdaQueryWrapper<UserDO> lqw = new LambdaQueryWrapper();
        lqw.eq(UserDO::getUsername,s);

        return (s == null||s.isEmpty()||userMapper.selectOne(lqw)==null);
    }

}
