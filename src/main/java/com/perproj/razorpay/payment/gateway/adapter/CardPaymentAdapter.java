package com.perproj.razorpay.payment.gateway.adapter;

import com.perproj.razorpay.payment.dto.request.PaymentRequest;
import com.perproj.razorpay.payment.gateway.PaymentAdapter;
import com.perproj.razorpay.payment.gateway.dto.PaymentResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class CardPaymentAdapter implements PaymentAdapter {
    @Override
    public PaymentResult initiate(PaymentRequest request) {
        return null;
    }
}
