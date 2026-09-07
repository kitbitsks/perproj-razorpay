package com.perproj.razorpay.payment.service;

import com.perproj.razorpay.payment.dto.request.CreateOrderRequest;
import com.perproj.razorpay.payment.dto.response.OrderResponse;
import com.perproj.razorpay.payment.dto.response.PaymentResponse;


import java.util.List;
import java.util.UUID;

public interface OrderService {

    OrderResponse create(UUID merchantId, CreateOrderRequest orderRequest);

    OrderResponse getById(UUID merchantId, UUID orderId);

    OrderResponse cancel(UUID merchantId, UUID orderId);

    List<PaymentResponse> listPayment(UUID merchantId, UUID orderId);



}
