package com.perproj.razorpay.vault.service;

import com.perproj.razorpay.vault.dto.request.TokenizeRequest;
import com.perproj.razorpay.vault.dto.response.TokenizeResponse;

import java.util.UUID;

public interface VaultService {

    TokenizeResponse tokenize(TokenizeRequest request, UUID merchantId);
}
