package com.perproj.razorpay.common.exception;

public class BusinessRuleViolationException extends RuntimeException{

    private String errorCode;
    private String errorMessage;

    public BusinessRuleViolationException(String errorCode, String errorMessage){
        super(errorMessage);
        this.errorCode = errorCode;
    }
}
