package com.perproj.razorpay.common.entity;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class Money {

    private int amountInPaise;
    private String currency;

    public Money(int amount, String currency) {
        this.amountInPaise = amount;
        this.currency = currency;
    }


    public static Money of(int amountInPaise, String currency) {
        return new Money(amountInPaise, currency);
    }

    public static Money inr(int amountInPaise) {
        return new Money(amountInPaise, "INR");
    }

    public Money add(Money other) {
        if (!this.currency.equals(other.currency)) {
            throw new IllegalArgumentException("Cannot add money with different currencies");
        }
        return new Money(this.amountInPaise + other.amountInPaise, this.currency);
    }

    public Money subtract(Money other) {
        if (!this.currency.equals(other.currency)) {
            throw new IllegalArgumentException("Cannot subtract money with different currencies");
        }
        return new Money(this.amountInPaise - other.amountInPaise, this.currency);
    }
}
