package com.perproj.razorpay.payment.service;

import com.perproj.razorpay.payment.dto.request.PaymentInitRequest;
import com.perproj.razorpay.payment.dto.response.PaymentResponse;

import java.util.UUID;

public interface PaymentService {

    PaymentResponse initiate(UUID merchantId, PaymentInitRequest initRequest);
}
