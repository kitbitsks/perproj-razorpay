package com.perproj.razorpay.payment.service.impl;

import com.perproj.razorpay.common.enums.OrderStatus;
import com.perproj.razorpay.common.exception.BusinessRuleViolationException;
import com.perproj.razorpay.common.exception.DuplicateResourceException;
import com.perproj.razorpay.common.exception.ResourceNotFoundException;
import com.perproj.razorpay.payment.dto.request.CreateOrderRequest;
import com.perproj.razorpay.payment.dto.response.OrderResponse;
import com.perproj.razorpay.payment.dto.response.PaymentResponse;
import com.perproj.razorpay.payment.entity.OrderRecord;
import com.perproj.razorpay.payment.entity.Payment;
import com.perproj.razorpay.payment.mapper.OrderMapper;
import com.perproj.razorpay.payment.mapper.PaymentMapper;
import com.perproj.razorpay.payment.repository.OrderRepository;
import com.perproj.razorpay.payment.repository.PaymentRepository;
import com.perproj.razorpay.payment.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private OrderRepository orderRepository;
    private OrderMapper orderMapper;
    private PaymentRepository paymentRepository;
    private PaymentMapper paymentMapper;


    @Override
    public OrderResponse create(UUID merchantId, CreateOrderRequest orderRequest) {
        if(orderRequest.receipt() !=null && orderRepository.existsByMerchantIdAndReceipt(merchantId,orderRequest.receipt())){
            throw new DuplicateResourceException("DUPLICATE_RESOURCE","Order with given recieptId already exists");
        }

        OrderRecord order = OrderRecord.builder()
                .orderStatus(OrderStatus.CREATED)
                .notes(orderRequest.notes())
                .money(orderRequest.amount())
                .merchantId(merchantId)
                .receipt(orderRequest.receipt())
                .expiresAt(orderRequest.expiresAt() != null ? orderRequest.expiresAt() :
                        LocalDateTime.now().plusMinutes(30))
                .build();

        order = orderRepository.save(order);
        //TODO : send kafka event
        return orderMapper.toResponse(order);
    }

    @Override
    public OrderResponse getById(UUID merchantId, UUID orderId) {
        OrderRecord order = orderRepository.findByIdAndMerchantId(orderId, merchantId)
                .orElseThrow(()-> new ResourceNotFoundException("Order", orderId));

       return orderMapper.toResponse(order);

    }

    @Override
    public OrderResponse cancel(UUID merchantId, UUID orderId) {
        OrderRecord order = orderRepository.findByIdAndMerchantId(orderId,merchantId)
                .orElseThrow(()-> new ResourceNotFoundException("Order", orderId));

        if(order.getOrderStatus().equals(OrderStatus.CANCELLED) || order.getOrderStatus().equals(OrderStatus.PAID)){
            throw new BusinessRuleViolationException("ORDER_CANNOT_CANCEL", "Cannot cancel order with orderId "+ orderId);
        }

        order.setOrderStatus(OrderStatus.CANCELLED);
        orderRepository.save(order);
        return orderMapper.toResponse(order);
    }

    @Override
    public List<PaymentResponse> listPayment(UUID merchantId, UUID orderId) {
        OrderRecord order = orderRepository.findByIdAndMerchantId(orderId,merchantId)
                .orElseThrow(()-> new ResourceNotFoundException("Order", orderId));

        List<Payment> payments = paymentRepository.findByOrder_Id(order);
        return paymentMapper.toResponse(payments);
    }
}
