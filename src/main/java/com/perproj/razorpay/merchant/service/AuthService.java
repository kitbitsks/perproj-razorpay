package com.perproj.razorpay.merchant.service;

import com.perproj.razorpay.merchant.dto.request.MerchantSignupRequest;
import com.perproj.razorpay.merchant.dto.response.MerchantResponse;

public interface AuthService {
    MerchantResponse signup(MerchantSignupRequest request);
}
