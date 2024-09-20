package com.thanhdat.quanlyhoctap.validator;

import com.thanhdat.quanlyhoctap.validator.constraint.SumMidFinalTermFactor;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.lang.reflect.Field;

public class SumMidFinalTermFactorValidator implements ConstraintValidator<SumMidFinalTermFactor, Object> {
    private String midTermFactorField;
    private String finalTermFactorField;
    private static final float SUM_RESULT = (float) 1.0;

    @Override
    public void initialize(SumMidFinalTermFactor constraintAnnotation) {
        midTermFactorField = constraintAnnotation.midTermFactorField();
        finalTermFactorField = constraintAnnotation.finalTermFactorField();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }
        try {
            Field firstFieldObj = value.getClass().getDeclaredField(this.midTermFactorField);
            Field secondFieldObj = value.getClass().getDeclaredField(this.finalTermFactorField);

            firstFieldObj.setAccessible(true);
            secondFieldObj.setAccessible(true);

            Float firstFieldValue = (Float) firstFieldObj.get(value);
            Float secondFieldValue = (Float) secondFieldObj.get(value);
            return firstFieldValue + secondFieldValue == SUM_RESULT;
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return false;
    }
}
