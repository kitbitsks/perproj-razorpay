package com.perproj.razorpay.vault.controller;

import com.perproj.razorpay.vault.dto.request.TokenizeRequest;
import com.perproj.razorpay.vault.dto.response.TokenizeResponse;
import com.perproj.razorpay.vault.service.VaultService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/v1/vault")
@RequiredArgsConstructor
public class VaultController {

    private final VaultService vaultService;
    private final UUID merchantId = UUID.randomUUID();

    @PostMapping
    public ResponseEntity<TokenizeResponse> tokenize(@Valid @RequestBody TokenizeRequest request){
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(vaultService.tokenize(request, merchantId));
    }
}
