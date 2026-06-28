package com.perproj.razorpay.merchant.service.impl;

import com.perproj.razorpay.merchant.mapper.ApiKeyMapper;
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

    private final ApiKeyMapper apiKeyMapper;

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
    public List<CreateApiKeyResponse> listOfApiKeys(UUID merchantId) {
        return apiKeyMapper.toResponse(apiKeyRepository.findByMerchant_Id(merchantId));
    }
}
