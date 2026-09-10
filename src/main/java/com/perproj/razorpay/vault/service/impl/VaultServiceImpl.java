package com.perproj.razorpay.vault.service.impl;

import com.perproj.razorpay.common.entity.Money;
import com.perproj.razorpay.common.enums.CardBrand;
import com.perproj.razorpay.common.exception.ResourceNotFoundException;
import com.perproj.razorpay.common.util.RandomizerUtil;
import com.perproj.razorpay.payment.processor.PaymentProcessorRouter;
import com.perproj.razorpay.payment.processor.dto.request.PaymentProcessorRequest;
import com.perproj.razorpay.payment.processor.dto.response.PaymentProcessorResponse;
import com.perproj.razorpay.vault.VaultEncryptorConfig;
import com.perproj.razorpay.vault.dto.request.TokenizeRequest;
import com.perproj.razorpay.vault.dto.response.TokenizeResponse;
import com.perproj.razorpay.vault.entity.CardToken;
import com.perproj.razorpay.vault.entity.VaultCard;
import com.perproj.razorpay.vault.repository.CardTokenRepository;
import com.perproj.razorpay.vault.repository.VaultRepository;
import com.perproj.razorpay.vault.service.VaultService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.encrypt.BytesEncryptor;
import org.springframework.security.crypto.keygen.KeyGenerators;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Map;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class VaultServiceImpl implements VaultService {

    private final BytesEncryptor dekEncryptor;
    private final VaultRepository vaultRepository;
    private final CardTokenRepository cardTokenRepository;
    private final PaymentProcessorRouter processorRouter;

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

        VaultCard vaultCard = vaultRepository.save(VaultCard.builder()
                        .cardHolderName(request.cardHolderName())
                        .brand(cardBrand)
                        .expiryYear(request.expiryYear().toString())
                        .expirtyMonth(request.expiryMonth().toString())
                        .encryptedDek(encryptedDek)
                        .encryptedPan(encryptedPan)
                        .bin(bin)
                        .lastFour(lastFour)
                .build());

        String token = "tok_" + RandomizerUtil.randomBase64(32);
        cardTokenRepository.save(CardToken.builder()
                        .vaultCard(vaultCard)
                        .token(token)
                        .merchant(merchantId)
                .build());

        return new TokenizeResponse(token,lastFour,cardBrand,request.expiryMonth(),request.expiryYear());

    }

    @Override
    public PaymentProcessorResponse charge(UUID paymentId, String token, Money money, Map<String, Object> methodDetails) {

        CardToken cardToken = cardTokenRepository.findByTokenAndRevokedAtIsNull(token)
                .orElseThrow(() -> new ResourceNotFoundException("CardToken", token));

        VaultCard vaultCard = cardToken.getVaultCard();
        byte[] panbytes = null;

        try{
            byte[] dek = dekEncryptor.decrypt(vaultCard.getEncryptedDek());
            panbytes = VaultEncryptorConfig.panEncrypter(dek).decrypt(vaultCard.getEncryptedPan());

            String pan = new String(panbytes, StandardCharsets.UTF_8);
            String expiry = vaultCard.getExpiryYear() + "/" + vaultCard.getExpirtyMonth();

            PaymentProcessorRequest processorRequest = PaymentProcessorRequest.card(
                    paymentId, pan, expiry,money,methodDetails
            );

            PaymentProcessorResponse processorResponse = processorRouter.charge(processorRequest);
            log.info("Vault charge registered, token={} ****", token.substring(0,4));
            return processorResponse;
        }
        catch(Exception e){
            log.warn("Vault charge failed, token={} ****", token.substring(0,4));
            return new PaymentProcessorResponse.Failure("VAULT_FAILED", e.getMessage());
        }
        finally{
            if(panbytes!=null) Arrays.fill(panbytes,(byte)0);
        }
    }

    private CardBrand detectBrand(String pan) {
        if (pan.startsWith("4")) return CardBrand.VISA;
        if (pan.startsWith("5") || pan.startsWith("2")) return CardBrand.MASTERCARD;
        if (pan.startsWith("37") || pan.startsWith("34")) return CardBrand.AMEX;
        return CardBrand.RUPAY;
    }
}
