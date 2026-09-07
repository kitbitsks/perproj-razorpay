package com.perproj.razorpay.payment.mapper;

import com.perproj.razorpay.payment.dto.response.OrderResponse;
import com.perproj.razorpay.payment.entity.OrderRecord;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface OrderMapper {

    OrderResponse toResponse(OrderRecord order);
}
