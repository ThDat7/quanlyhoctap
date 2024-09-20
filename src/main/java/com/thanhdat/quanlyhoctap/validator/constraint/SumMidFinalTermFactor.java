package com.thanhdat.quanlyhoctap.validator.constraint;

import com.thanhdat.quanlyhoctap.validator.SumMidFinalTermFactorValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = SumMidFinalTermFactorValidator.class)  // Validator logic class
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface SumMidFinalTermFactor {
    String message() default "Sum of fields is not valid";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
    String midTermFactorField();
    String finalTermFactorField();
}
