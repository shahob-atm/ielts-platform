package com.sh32bit.validation;

import com.sh32bit.enums.Role;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = RoleSubsetValidator.class)
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface RoleSubset {
    Role[] anyOf();
    String message() default "Role must be STUDENT or TEACHER";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
