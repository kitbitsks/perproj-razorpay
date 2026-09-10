package com.perproj.razorpay.vault.service;

import com.perproj.razorpay.common.entity.Money;
import com.perproj.razorpay.payment.processor.dto.response.PaymentProcessorResponse;
import com.perproj.razorpay.vault.dto.request.TokenizeRequest;
import com.perproj.razorpay.vault.dto.response.TokenizeResponse;

import java.util.Map;
import java.util.UUID;

public interface VaultService {

    TokenizeResponse tokenize(TokenizeRequest request, UUID merchantId);

    PaymentProcessorResponse charge(UUID paymentId, String token, Money money, Map<String, Object> methodDetails);
}
