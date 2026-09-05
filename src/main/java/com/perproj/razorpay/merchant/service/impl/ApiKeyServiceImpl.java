package com.perproj.razorpay.merchant.service.impl;

import com.perproj.razorpay.merchant.dto.response.ApiKeyResponse;
import com.perproj.razorpay.common.exception.ResourceNotFoundException;
import com.perproj.razorpay.common.util.RandomizerUtil;
import com.perproj.razorpay.merchant.dto.request.CreateApiKeyRequest;
import com.perproj.razorpay.merchant.dto.response.CreateApiKeyResponse;
import com.perproj.razorpay.merchant.entity.ApiKey;
import com.perproj.razorpay.merchant.entity.Merchant;
import com.perproj.razorpay.merchant.mapper.ApiKeyMapper;
import com.perproj.razorpay.merchant.repository.ApiKeyRepository;
import com.perproj.razorpay.merchant.repository.MerchantRepository;
import com.perproj.razorpay.merchant.service.ApiKeyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.beans.Transient;
import java.time.LocalDateTime;
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
                .orElseThrow(()-> new ResourceNotFoundException("merchant", merchantId));

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

        return apiKeyMapper.toCreateResponse(apiKey);
    }

    @Override
    public List<ApiKeyResponse> listOfApiKeys(UUID merchantId) {
        return apiKeyMapper.toResponseList(apiKeyRepository.findByMerchant_Id(merchantId));
    }

    @Override
    @Transactional
    public void revoke(UUID keyId, UUID merchantId) {
        ApiKey key = apiKeyRepository.findById(keyId)
                .filter(
                        apiKey -> apiKey.getMerchant().getId().equals(merchantId)
                )
                .orElseThrow(()->new ResourceNotFoundException("ApiKey",keyId));
        key.setEnabled(false);

    }

    @Override
    public CreateApiKeyResponse rotate(UUID keyId, UUID merchantId) {
       ApiKey key = apiKeyRepository.findById(keyId)
               .filter(
                       apiKey -> apiKey.getMerchant().getId().equals(merchantId)
               )
               .orElseThrow(() -> new ResourceNotFoundException("ApiKey", keyId));

       if(!key.isEnabled()){
           throw new RuntimeException("Cannot rotate disabled key");
       }

       String newRawSecret = RandomizerUtil.randomBase64(24);
       key.setPreviousKeySecretHash(key.getKeySecretHash());
       key.setKeySecretHash(newRawSecret);
       key.setRotatedAt(LocalDateTime.now());
       key.setGracePeriodExpiresAt(LocalDateTime.now().plusHours(24));

       key = apiKeyRepository.save(key);
       return apiKeyMapper.toCreateResponse(key);
    }
}
