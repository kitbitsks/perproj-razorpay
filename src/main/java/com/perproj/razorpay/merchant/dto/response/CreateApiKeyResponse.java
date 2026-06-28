package com.perproj.razorpay.merchant.dto.response;

import com.perproj.razorpay.common.enums.Environment;

import java.util.UUID;

public record CreateApiKeyResponse(
        UUID id,
        String keyId,
        String keySecret,
        Environment environment
) {
}
