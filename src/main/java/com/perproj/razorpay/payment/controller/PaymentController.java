package com.perproj.razorpay.payment.controller;

import com.perproj.razorpay.payment.dto.request.PaymentInitRequest;
import com.perproj.razorpay.payment.dto.response.PaymentResponse;
import com.perproj.razorpay.payment.service.PaymentService;
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
@RequestMapping("v1/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final UUID merchantId = UUID.randomUUID();
    private PaymentService paymentService;

    @PostMapping
    public ResponseEntity<PaymentResponse> initiate(@Valid @RequestBody PaymentInitRequest initRequest){
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(paymentService.initiate(merchantId,initRequest));
    }
}
