package com.perproj.razorpay.payment.mapper;

import com.perproj.razorpay.payment.dto.response.PaymentResponse;
import com.perproj.razorpay.payment.entity.Payment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface PaymentMapper {

    @Mapping(target = "orderId", source = "order.id")
    List<PaymentResponse> toResponse(List<Payment> payment);
}
