package com.perproj.razorpay.payment.service.impl;

import com.perproj.razorpay.common.enums.EventAggregateType;
import com.perproj.razorpay.common.enums.OrderStatus;
import com.perproj.razorpay.common.enums.PaymentEvent;
import com.perproj.razorpay.common.enums.PaymentStatus;
import com.perproj.razorpay.common.exception.BusinessRuleViolationException;
import com.perproj.razorpay.common.exception.ResourceNotFoundException;
import com.perproj.razorpay.payment.dto.request.PaymentInitRequest;
import com.perproj.razorpay.payment.dto.request.PaymentRequest;
import com.perproj.razorpay.payment.dto.response.PaymentResponse;
import com.perproj.razorpay.payment.entity.OrderRecord;
import com.perproj.razorpay.payment.entity.Payment;
import com.perproj.razorpay.payment.outbox.OutboxEventPublisher;
import com.perproj.razorpay.payment.repository.OrderRepository;
import com.perproj.razorpay.payment.repository.PaymentRepository;
import com.perproj.razorpay.payment.service.PaymentService;
import com.perproj.razorpay.payment.statemachine.PaymentTransitionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private OrderRepository orderRepository;
    private PaymentRepository paymentRepository;
    private PaymentTransitionService paymentTransitionService;
    private OutboxEventPublisher eventPublisher;

    @Override
    @Transactional
    public PaymentResponse initiate(UUID merchantId, PaymentInitRequest initRequest) {
        OrderRecord order = orderRepository.findByIdAndMerchantId(initRequest.orderId(), merchantId)
                .orElseThrow(()-> new ResourceNotFoundException("OrderId", initRequest.orderId()));

        if(order.getOrderStatus() != OrderStatus.CREATED && order.getOrderStatus() != OrderStatus.ATTEMPTED){
            throw new BusinessRuleViolationException("ORDER_NOT_PAYABLE", "Order cannot accept payment in "+ order.getOrderStatus());
        }

        order.setOrderStatus(OrderStatus.ATTEMPTED);
        order.setAttempts(order.getAttempts()+1);

        Payment payment = Payment.builder()
                .paymentMethod(initRequest.method())
                .merchantId(merchantId)
                .order(order)
                .amount(order.getAmount())
                .status(PaymentStatus.CREATED)
                .idempotencyKey(UUID.randomUUID().toString())
                .methodDetails(initRequest.methodDetails())
                .build();
        payment = paymentRepository.save(payment);

        eventPublisher.publish(EventAggregateType.PAYMENT, payment.getId(), "PAYMENT_CREATED",
                Map.of("orderId", order.getId().toString(),
                        "paymentId", payment.getId().toString(),
                        "merchantId", merchantId.toString(),
                        "paymentStatus", payment.getStatus().name(),
                        "amountUnits", order.getAmount().getAmountUnits(),
                        "amountCurrency", order.getAmount().getCurrency(),
                        "paymentMethod", payment.getPaymentMethod()
                )
        );

        PaymentRequest paymentRequest = new PaymentRequest(payment.getId(),
                initRequest.orderId(), merchantId,
                order.getAmount(), initRequest.method(),
                initRequest.methodDetails());

        paymentTransitionService.apply(payment , PaymentEvent.AUTHORIZE_ATTEMPT);



        return null;
    }
}
