package com.perproj.razorpay.merchant.entity;

import com.perproj.razorpay.common.enums.Environment;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name="api_key")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApiKey {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "merchant_id", nullable = false)
    private Merchant merchant;

    @Column(nullable = false, length = 100, unique = true)
    private String keyId;

    @Column(nullable = false, length = 100)
    private String keySecretHash;

    @Column(nullable = false, length = 10)
    @Enumerated(EnumType.STRING)
    private Environment environment;

    @Column(nullable = false)
    boolean enabled;

    LocalDateTime lastUsedAt;
    LocalDateTime createdAt;
    LocalDateTime gracePeriodExpiresAt;


}
