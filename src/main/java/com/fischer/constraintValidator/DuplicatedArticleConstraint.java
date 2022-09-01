package com.fischer.constraintValidator;

import org.springframework.validation.annotation.Validated;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = DuplicatedArticleValidator.class)
@Target({ElementType.METHOD,ElementType.FIELD,ElementType.PARAMETER,ElementType.TYPE_USE})
public @interface DuplicatedArticleConstraint {
    String message()default "已经有人起过相同标题的文章了~";

    Class<?>[] groups() default { };
    Class<? extends Payload>[] payload() default { };

}
