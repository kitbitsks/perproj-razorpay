package com.perproj.razorpay.operations.entity;

import jakarta.persistence.Embeddable;
import jakarta.persistence.EmbeddedId;

import java.util.UUID;

@Embeddable
public class SettlementPaymentId {

    private UUID settlementId;

    private UUID paymentId;
}
