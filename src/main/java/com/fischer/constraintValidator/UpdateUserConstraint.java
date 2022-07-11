package com.fischer.constraintValidator;


import javax.validation.Constraint;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Constraint(validatedBy = UpdateUserValidator.class)
@Retention(RetentionPolicy.RUNTIME)
public @interface UpdateUserConstraint {
    String message() default "该用户名已被人使用";

    Class[] groups() default {};

    Class[] payload() default {};
}
