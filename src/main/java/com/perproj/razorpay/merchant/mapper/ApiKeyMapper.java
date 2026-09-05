package com.perproj.razorpay.merchant.mapper;
import com.perproj.razorpay.merchant.dto.response.ApiKeyResponse;
import com.perproj.razorpay.merchant.dto.response.CreateApiKeyResponse;
import com.perproj.razorpay.merchant.entity.ApiKey;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;


import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ApiKeyMapper {

    @Mapping(source = "keySecretHash", target = "keySecret")
    CreateApiKeyResponse toCreateResponse(ApiKey apiKey);

    List<ApiKeyResponse> toResponseList(List<ApiKey> apiKeyList);
}
