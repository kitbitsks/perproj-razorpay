package com.perproj.razorpay.payment.config;

import com.perproj.razorpay.common.enums.PaymentMethod;
import com.perproj.razorpay.payment.gateway.PaymentAdapter;
import com.perproj.razorpay.payment.processor.PaymentProcessor;
import com.perproj.razorpay.payment.processor.adapter.CardPaymentProcessor;
import com.perproj.razorpay.payment.processor.adapter.NetBankingPaymentProcessor;
import com.perproj.razorpay.payment.processor.adapter.UPIPaymentProcessor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
@RequiredArgsConstructor
public class PaymentProcessorConfig {

    private final CardPaymentProcessor cardPaymentProcessor;
    private final NetBankingPaymentProcessor netBankingPaymentProcessor;
    private final UPIPaymentProcessor upiPaymentProcessor;

    @Bean
    public Map<PaymentMethod, PaymentProcessor> paymentProcessorMap(){
        return Map.of(
                PaymentMethod.CARD, cardPaymentProcessor,
                PaymentMethod.NETBANKING, netBankingPaymentProcessor,
                PaymentMethod.UPI, upiPaymentProcessor
        );
    }
}
