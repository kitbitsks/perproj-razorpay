package com.perproj.razorpay.merchant.service.impl;

import com.perproj.razorpay.common.enums.MerchantStatus;
import com.perproj.razorpay.common.enums.UserRole;
import com.perproj.razorpay.common.exception.DuplicateResourceException;
import com.perproj.razorpay.merchant.dto.request.MerchantSignupRequest;
import com.perproj.razorpay.merchant.dto.response.MerchantResponse;
import com.perproj.razorpay.merchant.entity.AppUser;
import com.perproj.razorpay.merchant.entity.Merchant;
import com.perproj.razorpay.merchant.repository.AppUserRepository;
import com.perproj.razorpay.merchant.repository.MerchantRepository;
import com.perproj.razorpay.merchant.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final AppUserRepository appUserRepository;
    private final MerchantRepository merchantRepository;

    @Override
    public MerchantResponse signup(MerchantSignupRequest request) {
        if (merchantRepository.existsByEmail(request.email())) {
            throw new DuplicateResourceException("DUPLICATE_EMAIL", "Email already exists");
        }

        Merchant merchant = Merchant.builder()
                .businessName(request.businessName())
                .businessType(request.businessType())
                .name(request.name())
                .email(request.email())
                .status(MerchantStatus.PENDING_KYC)
                .build();

        merchant = merchantRepository.save(merchant);

        AppUser appUser = AppUser.builder()
                        .username(request.name())
                        .email(request.email())
                        .merchant(merchant)
                        .passwordHash(request.password())
                        .role(UserRole.OWNER)
                        .build();
        appUserRepository.save(appUser);

        return new MerchantResponse(merchant.getId(),
                merchant.getName(),
                merchant.getEmail(),
                merchant.getBusinessName(),
                merchant.getBusinessType(), merchant.getStatus()
        );
    }

}
