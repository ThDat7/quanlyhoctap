package com.thanhdat.quanlyhoctap.exception;

import com.thanhdat.quanlyhoctap.dto.response.ApiResponse;
import com.thanhdat.quanlyhoctap.exception.code.ValidationErrorCode;
import jakarta.validation.ConstraintViolation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Map;
import java.util.Objects;
import java.util.Set;

@ControllerAdvice
public class GlobalExceptionHandler {
    private static final Set<String> ATTRIBUTES = Set.of("value", "min", "max");

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    ResponseEntity<ApiResponse> handlingValidation(MethodArgumentNotValidException exception) {
        String enumKey = exception.getFieldError().getDefaultMessage();

        ValidationErrorCode validationErrorCode = ValidationErrorCode.INVALID_KEY;
        Map<String, Object> attributes = null;
        try {
            validationErrorCode = ValidationErrorCode.valueOf(enumKey);

            var constraintViolation = exception.getBindingResult()
                    .getAllErrors().get(0).unwrap(ConstraintViolation.class);

            attributes = constraintViolation.getConstraintDescriptor().getAttributes();
        } catch (IllegalArgumentException e){

        }

        ApiResponse apiResponse = new ApiResponse();

        apiResponse.setCode(validationErrorCode.getCode());
        apiResponse.setMessage(Objects.nonNull(attributes) ?
                mapAttribute(validationErrorCode.getBaseMessage(), attributes)
                : validationErrorCode.getBaseMessage());

        return ResponseEntity.badRequest().body(apiResponse);
    }

    private String mapAttribute(String baseMessage, Map<String, Object> attributes){
        String message = baseMessage;

        for (String attribute : ATTRIBUTES) {
            String value = String.valueOf(attributes.get(attribute));
            message = message.replace("{" + attribute + "}", value);
        }
        return message;
    }
}
