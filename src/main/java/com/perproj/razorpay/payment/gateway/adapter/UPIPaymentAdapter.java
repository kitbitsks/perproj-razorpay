package com.perproj.razorpay.payment.gateway.adapter;

import com.perproj.razorpay.common.enums.PaymentMethod;
import com.perproj.razorpay.payment.dto.request.PaymentRequest;
import com.perproj.razorpay.payment.gateway.PaymentAdapter;
import com.perproj.razorpay.payment.gateway.dto.PaymentResult;
import com.perproj.razorpay.payment.processor.PaymentProcessorRouter;
import com.perproj.razorpay.payment.processor.dto.request.PaymentProcessorRequest;
import com.perproj.razorpay.payment.processor.dto.response.PaymentProcessorResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class UPIPaymentAdapter implements PaymentAdapter {
    private final PaymentProcessorRouter paymentProcessorRouter;

    @Override
    public PaymentResult initiate(PaymentRequest request) {
        log.info("Initited payment with UPI adapter, paymentId : "+ request.paymentId());

        try{
            PaymentProcessorRequest paymentProcessorRequest = PaymentProcessorRequest.nonCard(
                    request.paymentId(),
                    PaymentMethod.UPI,
                    request.amount(),
                    request.methodDetails()
            );
            PaymentProcessorResponse paymentProcessorResponse =  paymentProcessorRouter.charge(paymentProcessorRequest);

            return switch (paymentProcessorResponse){
                case PaymentProcessorResponse.Failure failure -> new PaymentResult.Failure(failure.errorCode(), failure.errorDescription());
                case PaymentProcessorResponse.Success success -> new PaymentResult.Success(success.bankReference());
                case PaymentProcessorResponse.Pending pending -> new PaymentResult.Pending(pending.processorReference());
            };
        }
        catch(Exception e){
            log.warn("UPI failed, paymentId "+request.paymentId());
            return new PaymentResult.Failure("UPI_FAILED", e.getMessage());
        }
    }
}
