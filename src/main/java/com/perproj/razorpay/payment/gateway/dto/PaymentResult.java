package com.perproj.razorpay.payment.gateway.dto;

public sealed interface PaymentResult permits PaymentResult.Failure , PaymentResult.Success, PaymentResult.Pending {

    record Pending(String registrationRef) implements PaymentResult {};
    record Success(String bankRef) implements  PaymentResult {};
    record Failure(String errorCode, String errorDescription) implements  PaymentResult {};
}
