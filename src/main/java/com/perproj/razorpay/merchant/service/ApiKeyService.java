package com.perproj.razorpay.merchant.service;

import com.perproj.razorpay.merchant.dto.request.CreateApiKeyRequest;
import com.perproj.razorpay.merchant.dto.response.CreateApiKeyResponse;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.List;

public interface ApiKeyService {
    CreateApiKeyResponse create(UUID merchantId, CreateApiKeyRequest apiKeyRequest);

    List<CreateApiKeyResponse> listOfApiKeys(UUID merchantId);
}
