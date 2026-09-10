package com.perproj.razorpay.vault.dto.response;

import com.perproj.razorpay.common.enums.CardBrand;

public record TokenizeResponse(

        String token,
        String lastFour,
        CardBrand brand,
        Integer expiryMonth,
        Integer expiryYear

) {
}
