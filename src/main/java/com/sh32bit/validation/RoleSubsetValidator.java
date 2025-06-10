package com.sh32bit.validation;

import com.sh32bit.enums.Role;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Arrays;

public class RoleSubsetValidator implements ConstraintValidator<RoleSubset, Role> {
    private Role[] subset;

    @Override
    public void initialize(RoleSubset constraint) {
        this.subset = constraint.anyOf();
    }

    @Override
    public boolean isValid(Role value, ConstraintValidatorContext context) {
        if (value == null) return false;
        return Arrays.asList(subset).contains(value);
    }
}
