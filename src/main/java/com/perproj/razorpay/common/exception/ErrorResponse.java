package com.perproj.razorpay.common.exception;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDateTime;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ErrorResponse(
        String errorCode,
        String errorMessage,
        LocalDateTime timestamp,
        List<FieldError> fieldError
        ) {

    public record FieldError(String field, String errorMessage){}

    public static ErrorResponse of(String errorCode, String errorMessage){
        return new ErrorResponse(errorCode,errorMessage,LocalDateTime.now(),null);
    }

    public static ErrorResponse of(String errorCode, String errorMessage, List<FieldError> fieldError){
        return new ErrorResponse(errorCode,errorMessage,LocalDateTime.now(),fieldError);
    }
}
