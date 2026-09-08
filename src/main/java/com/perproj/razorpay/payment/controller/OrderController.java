package com.perproj.razorpay.payment.controller;

import com.perproj.razorpay.payment.dto.request.CreateOrderRequest;
import com.perproj.razorpay.payment.dto.response.OrderResponse;
import com.perproj.razorpay.payment.service.OrderService;
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
@RequestMapping("/v1/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final UUID merchantId = UUID.randomUUID();

    @PostMapping("/create")
    public ResponseEntity<OrderResponse> create(@RequestBody @Valid CreateOrderRequest orderRequest){
        return ResponseEntity.status(HttpStatus.CREATED).body(
                orderService.create(merchantId, orderRequest)
        );
    }
}
