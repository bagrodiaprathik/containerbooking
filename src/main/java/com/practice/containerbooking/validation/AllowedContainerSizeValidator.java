package com.practice.containerbooking.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Set;

/**
 * Implements the validation logic for the @AllowedContainerSize annotation.
 */
public class AllowedContainerSizeValidator implements ConstraintValidator<AllowedContainerSize, Integer> {

    // A set of the allowed values
    private static final Set<Integer> ALLOWED_VALUES = Set.of(20, 40);

    @Override
    public void initialize(AllowedContainerSize constraintAnnotation) {
        // No initialization needed
    }

    @Override
    public boolean isValid(Integer value, ConstraintValidatorContext context) {
        // If the value is null, we let @NotNull handle it.
        // This validator only checks non-null values.
        if (value == null) {
            return true;
        }

        // The core logic: checks if the incoming value is in our allowed set.
        return ALLOWED_VALUES.contains(value);
    }
}
