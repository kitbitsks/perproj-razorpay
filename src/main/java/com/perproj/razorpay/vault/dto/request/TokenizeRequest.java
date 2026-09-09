package com.perproj.razorpay.vault.dto.request;

import com.perproj.razorpay.vault.validation.ExpiryYear;
import jakarta.validation.constraints.*;
import org.hibernate.validator.constraints.LuhnCheck;

public record TokenizeRequest(

       @NotBlank(message = "PAN is required")
       @LuhnCheck
       @Pattern(regexp = "^[0-9]{13,19}$", message = "PAN length is invalid")
       String pan,

       @NotBlank(message = "CVV is required")
       @Pattern(regexp = "^[0-9]{3,4}$", message = "CVV length is invalid")
       String cvv,

       @NotNull(message = "Expiry year is required")
       @ExpiryYear
       Integer expiryYear,

       @NotNull(message = "Expiry month is required")
       @Min(value=1, message = "Expiry must be between 1 to 12")
       @Max(value = 12, message =  "Expirty must be between 1 to 12")
       Integer expiryMonth,

       @Size(min=3, message = "cardholder name should have atleast 3 letters")
       String cardHolderName
) { }
