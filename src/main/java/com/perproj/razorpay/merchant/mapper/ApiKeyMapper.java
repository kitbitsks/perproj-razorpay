package com.perproj.razorpay.merchant.mapper;
import com.perproj.razorpay.merchant.dto.response.CreateApiKeyResponse;
import com.perproj.razorpay.merchant.entity.ApiKey;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;


import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ApiKeyMapper {

    List<CreateApiKeyResponse> toResponse(List<ApiKey> byMerchantId);
}
