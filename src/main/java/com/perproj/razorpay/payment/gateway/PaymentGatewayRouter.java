package com.perproj.razorpay.payment.gateway;

import com.perproj.razorpay.common.enums.PaymentMethod;
import com.perproj.razorpay.payment.dto.request.PaymentRequest;
import com.perproj.razorpay.payment.entity.Payment;
import com.perproj.razorpay.payment.gateway.dto.PaymentResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class PaymentGatewayRouter {

    private final Map<PaymentMethod, PaymentAdapter> paymentAdapters;

    public PaymentResult initiate(PaymentRequest request){
        PaymentAdapter adapter = paymentAdapters.get(request.method());
        if(adapter == null){
            throw new IllegalArgumentException("No payment adapter registered with this method : " + request.method());
        }
        return adapter.initiate(request);
    }
}
