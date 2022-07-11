package com.fischer.constraintValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * @author fisher
 */
@Constraint(validatedBy = DuplicatedUsernameValidator.class)
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD,ElementType.FIELD,ElementType.PARAMETER,ElementType.TYPE_USE})
public @interface DuplicatedUsernameConstraint {
    String message()default "该用户名已经被人使用";

    Class<?>[] groups() default { };
    Class<? extends Payload>[] payload() default { };
}
