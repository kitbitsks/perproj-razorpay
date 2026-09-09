package com.perproj.razorpay.vault;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.encrypt.AesBytesEncryptor;
import org.springframework.security.crypto.encrypt.BytesEncryptor;
import org.springframework.security.crypto.keygen.KeyGenerators;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

@Configuration
public class VaultEncryptorConfig {

    public static BytesEncryptor panEncrypter(byte[] dek) {
        SecretKeySpec decKey = new SecretKeySpec(dek, "AES");
        return new AesBytesEncryptor(decKey, KeyGenerators.secureRandom(12),
                AesBytesEncryptor.CipherAlgorithm.GCM);
    }
}
