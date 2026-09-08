package com.perproj.razorpay.payment.config;

import com.perproj.razorpay.common.enums.PaymentMethod;
import com.perproj.razorpay.payment.gateway.PaymentAdapter;
import com.perproj.razorpay.payment.gateway.adapter.CardPaymentAdapter;
import com.perproj.razorpay.payment.gateway.adapter.NetBankingAdapter;
import com.perproj.razorpay.payment.gateway.adapter.UPIPaymentAdapter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
@RequiredArgsConstructor
public class PaymentAdapterConfig {

    private final CardPaymentAdapter cardPaymentAdapter;
    private final NetBankingAdapter netBankingAdapter;
    private final UPIPaymentAdapter upiPaymentAdapter;

    @Bean
    public Map<PaymentMethod, PaymentAdapter> paymentAdapterMap(){
        return Map.of(
                PaymentMethod.CARD, cardPaymentAdapter,
                PaymentMethod.NETBANKING, netBankingAdapter,
                PaymentMethod.UPI, upiPaymentAdapter
        );
    }
}
