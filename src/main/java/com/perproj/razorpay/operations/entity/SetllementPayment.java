package com.perproj.razorpay.operations.entity;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import java.util.UUID;

@Entity
@Table(name = "settlement_payment")
public class SetllementPayment {

    @EmbeddedId
    private SettlementPaymentId id;
}
