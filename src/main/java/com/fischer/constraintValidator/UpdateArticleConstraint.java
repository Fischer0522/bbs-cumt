package com.fischer.constraintValidator;

import javax.validation.Constraint;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author fisher
 */
@Constraint(validatedBy = UpdateArticleValidator.class)
@Retention(RetentionPolicy.RUNTIME)
public @interface UpdateArticleConstraint {
    String message() default "已经有人取过相同的标题了";

    Class[] groups() default {};

    Class[] payload() default {};

}
