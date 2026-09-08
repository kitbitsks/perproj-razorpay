package com.perproj.razorpay.payment.processor;

import com.perproj.razorpay.payment.processor.dto.request.PaymentProcessorRequest;
import com.perproj.razorpay.payment.processor.dto.response.PaymentProcessorResponse;

public interface PaymentProcessor {

    PaymentProcessorResponse charge(PaymentProcessorRequest request);
}
