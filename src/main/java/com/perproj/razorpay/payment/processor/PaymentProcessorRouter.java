package com.perproj.razorpay.payment.processor;

import com.perproj.razorpay.payment.gateway.PaymentAdapter;
import com.perproj.razorpay.payment.processor.dto.request.PaymentProcessorRequest;
import com.perproj.razorpay.payment.processor.dto.response.PaymentProcessorResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@Slf4j
@RequiredArgsConstructor
public class PaymentProcessorRouter {

    private final Map<PaymentProcessorResponse, PaymentProcessor> paymentProcessorRouterMap;

    public PaymentProcessorResponse charge(PaymentProcessorRequest request){
        PaymentProcessor processorAdapter = paymentProcessorRouterMap.get(request.method());
        if(processorAdapter == null){
            throw new IllegalArgumentException("Processor with payment method not present "+ request.method());
        }

        return processorAdapter.charge(request);
    }
}
