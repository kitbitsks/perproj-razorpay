package com.perproj.razorpay.merchant.service.impl;

import com.perproj.razorpay.merchant.dto.response.ApiKeyResponse;
import com.perproj.razorpay.common.exception.ResourceNotFoundException;
import com.perproj.razorpay.common.util.RandomizerUtil;
import com.perproj.razorpay.merchant.dto.request.CreateApiKeyRequest;
import com.perproj.razorpay.merchant.dto.response.CreateApiKeyResponse;
import com.perproj.razorpay.merchant.entity.ApiKey;
import com.perproj.razorpay.merchant.entity.Merchant;
import com.perproj.razorpay.merchant.repository.ApiKeyRepository;
import com.perproj.razorpay.merchant.repository.MerchantRepository;
import com.perproj.razorpay.merchant.service.ApiKeyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class ApiKeyServiceImpl implements ApiKeyService {

    private final MerchantRepository merchantRepository;

    private final ApiKeyRepository apiKeyRepository;

//    private final ApiKeyMapper apiKeyMapper;

    @Override
    public CreateApiKeyResponse create(UUID merchantId, CreateApiKeyRequest apiKeyRequest) {
        Merchant merchant = merchantRepository.findById(merchantId)
                .orElseThrow(()-> new ResourceNotFoundException(merchantId, "merchant"));

       //need to create secret key and secret hash
        String keyId = "rzp_"+apiKeyRequest.environment()+"_"+ RandomizerUtil.randomBase64(24);
        String keySecret = RandomizerUtil.randomBase64(40);

        ApiKey apiKey = ApiKey.builder()
                .merchant(merchant)
                .keyId(keyId)
                .keySecretHash(keySecret)
                .environment(apiKeyRequest.environment())
                .build();

        apiKey =  apiKeyRepository.save(apiKey);

        return new CreateApiKeyResponse(apiKey.getId(), apiKey.getKeyId(), apiKey.getKeySecretHash(), apiKey.getEnvironment());
    }

    @Override
    public List<ApiKeyResponse> listOfApiKeys(UUID merchantId) {
        List<ApiKey> apiKey = apiKeyRepository.findByMerchant_Id(merchantId);
        List<ApiKeyResponse> apiKeyResponse = apiKey.stream().map(
                apiKey1 ->
                    new ApiKeyResponse(apiKey1.getId(), apiKey1.getKeyId(), apiKey1.getEnvironment(), apiKey1.isEnabled(), apiKey1.getLastUsedAt(), apiKey1.getCreatedAt())
                ).toList();
        return apiKeyResponse;
    }
}
