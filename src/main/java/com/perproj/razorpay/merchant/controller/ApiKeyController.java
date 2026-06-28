package com.perproj.razorpay.merchant.controller;

import com.perproj.razorpay.merchant.dto.request.CreateApiKeyRequest;
import com.perproj.razorpay.merchant.dto.response.CreateApiKeyResponse;
import com.perproj.razorpay.merchant.service.ApiKeyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;
import java.util.List;

@RestController
@RequestMapping("/v1/merchants/{merchantId}/api-keys")
@RequiredArgsConstructor
public class ApiKeyController {

    private final ApiKeyService apiKeyService;

    @PostMapping
    public ResponseEntity<CreateApiKeyResponse> create(@PathVariable("merchantId") UUID merchantId, @Valid @RequestBody CreateApiKeyRequest apiKeyRequest){
        return ResponseEntity.status(HttpStatus.CREATED).body( apiKeyService.create(merchantId, apiKeyRequest));
    }

    @GetMapping
    public ResponseEntity<List<CreateApiKeyResponse>> listOfApiKey(@PathVariable("merchantId") UUID merchantId){
        return ResponseEntity.ok().body(apiKeyService.listOfApiKeys(merchantId));
    }
}
