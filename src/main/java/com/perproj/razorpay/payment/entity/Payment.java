package com.perproj.razorpay.payment.entity;

import com.perproj.razorpay.common.enums.PaymentMethod;
import jakarta.persistence.*;

import java.sql.SQLType;
import java.time.LocalDateTime;
import java.util.*;
import com.perproj.razorpay.common.entity.Money;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "payment")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional =false)
    @JoinColumn(name = "order_id", nullable = false)
    private OrderRecord orderRecord;

    @Column(nullable = false)
    private UUID merchantId;

    @Embedded
    private Money money;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentMethod paymentMethod;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "method_details", columnDefinition = "jsonb")
    private Map<String, Object> methodDetails;

    @Column(length = 100)
    private String bankReference;

    @Column(length = 100)
    private String errorCode;

    @Column(length = 100)
    private String errorDescription;

    private LocalDateTime authorizedAt;
    private LocalDateTime capturedAt;
    private LocalDateTime failedAt;
    private LocalDateTime refundedAt;
    private LocalDateTime settledAt;

}
