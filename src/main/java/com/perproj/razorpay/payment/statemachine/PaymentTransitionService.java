package com.perproj.razorpay.payment.statemachine;

import com.perproj.razorpay.common.enums.PaymentEvent;
import com.perproj.razorpay.common.enums.PaymentStatus;
import com.perproj.razorpay.payment.entity.Payment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentTransitionService {

    private final PaymentStateMachine paymentStateMachine;

    public PaymentStatus apply(Payment payment, PaymentEvent event) {
        PaymentStatus next = paymentStateMachine.transition(payment.getStatus(), event);
        return next;
    }
}
