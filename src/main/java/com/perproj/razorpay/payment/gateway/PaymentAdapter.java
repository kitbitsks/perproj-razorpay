package com.perproj.razorpay.payment.gateway;

import com.perproj.razorpay.payment.dto.request.PaymentRequest;
import com.perproj.razorpay.payment.gateway.dto.PaymentResult;

public interface PaymentAdapter {

    PaymentResult initiate(PaymentRequest request);
}
