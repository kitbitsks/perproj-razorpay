package com.perproj.razorpay.vault.service.impl;

import com.perproj.razorpay.common.enums.CardBrand;
import com.perproj.razorpay.vault.VaultEncryptorConfig;
import com.perproj.razorpay.vault.dto.request.TokenizeRequest;
import com.perproj.razorpay.vault.dto.response.TokenizeResponse;
import com.perproj.razorpay.vault.service.VaultService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.encrypt.BytesEncryptor;
import org.springframework.security.crypto.keygen.KeyGenerators;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class VaultServiceImpl implements VaultService {

    private final BytesEncryptor dekEncryptor;

    @Override
    public TokenizeResponse tokenize(TokenizeRequest request, UUID merchantId) {
        String lastFour = request.pan().substring(request.pan().length()-4);
        String bin = request.pan().substring(0,6);
        CardBrand cardBrand = detectBrand(request.pan());

        byte[] dek = KeyGenerators.secureRandom(32).generateKey();
        byte[] encryptedPan = VaultEncryptorConfig.panEncrypter(dek).encrypt(
                request.pan().getBytes(StandardCharsets.UTF_8)
        );
        byte[] encryptedDek = dekEncryptor.encrypt(dek);



    }

    private CardBrand detectBrand(String pan) {
        if (pan.startsWith("4")) return CardBrand.VISA;
        if (pan.startsWith("5") || pan.startsWith("2")) return CardBrand.MASTERCARD;
        if (pan.startsWith("37") || pan.startsWith("34")) return CardBrand.AMEX;
        return CardBrand.RUPAY;
    }
}
