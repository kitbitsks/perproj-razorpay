package com.perproj.razorpay.payment.processor.dto.response;

public sealed interface PaymentProcessorResponse permits PaymentProcessorResponse.Success, PaymentProcessorResponse.Failure, PaymentProcessorResponse.Pending {

    record Success(String processorReference, String bankReference) implements  PaymentProcessorResponse{};
    record Failure(String errorCode, String errorDescription) implements  PaymentProcessorResponse{};
    record Pending(String processorReference) implements  PaymentProcessorResponse{};}
