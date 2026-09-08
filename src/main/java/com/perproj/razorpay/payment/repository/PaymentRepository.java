package com.perproj.razorpay.payment.repository;

import com.perproj.razorpay.payment.entity.OrderRecord;
import com.perproj.razorpay.payment.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;
import java.util.List;

public interface PaymentRepository extends JpaRepository<Payment, UUID> {

    List<Payment> findByOrder_Id(OrderRecord order);
}
