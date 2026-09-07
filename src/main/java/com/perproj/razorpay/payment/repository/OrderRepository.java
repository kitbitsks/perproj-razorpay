package com.perproj.razorpay.payment.repository;

import com.perproj.razorpay.payment.entity.OrderRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;
import java.util.Optional;


public interface OrderRepository extends JpaRepository<OrderRecord, UUID> {

    boolean existsByMerchantIdAndReceipt(UUID merchantId, String receipt);

    Optional<OrderRecord> findByIdAndMerchantId(UUID orderId, UUID merchantId);
}

