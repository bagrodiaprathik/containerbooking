package com.practice.containerbooking.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Validates that the container size is one of the allowed values (20 or 40).
 */
@Target({ ElementType.FIELD }) // This annotation can only be applied to fields
@Retention(RetentionPolicy.RUNTIME) // This annotation will be available at runtime for validation
@Constraint(validatedBy = AllowedContainerSizeValidator.class) // Points to the class that holds the logic
public @interface AllowedContainerSize {

    // Default error message if validation fails
    String message() default "Container size must be either 20 or 40";

    // Required by validation spec (for grouping validations)
    Class<?>[] groups() default {};

    // Required by validation spec (for attaching metadata)
    Class<? extends Payload>[] payload() default {};
}
