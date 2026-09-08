package com.perproj.razorpay.payment.processor.adapter;

import com.perproj.razorpay.payment.processor.PaymentProcessor;
import com.perproj.razorpay.payment.processor.dto.request.PaymentProcessorRequest;
import com.perproj.razorpay.payment.processor.dto.response.PaymentProcessorResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class UPIPaymentProcessor implements PaymentProcessor {

    @Override
    public PaymentProcessorResponse charge(PaymentProcessorRequest request) {
        return null;
    }
}
