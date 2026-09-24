package com.perproj.razorpay.payment.outbox;

import com.perproj.razorpay.common.enums.OutboxStatus;
import com.perproj.razorpay.payment.entity.OutboxEvent;
import com.perproj.razorpay.payment.repository.OutboxEventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class OutboxResultHandler {

    private final OutboxEventRepository eventRepository;
    private final Integer MAX_ATTEMPT = 3;

    @Transactional
    public void handleEventFailed(OutboxEvent event, String message) {
        event.setAttempt(event.getAttempt()+1);
        event.setLastErrorAt(
                message.length() < 1000 ? message : message.substring(0,1000)
        );

        if(event.getAttempt() >= MAX_ATTEMPT){
            event.setStatus(OutboxStatus.FAILED);
        }
        eventRepository.save(event);
    }

    @Transactional
    public void handleEventPublished(OutboxEvent event) {
        event.setStatus(OutboxStatus.PUBLISHED);
        event.setPublishedAt(LocalDateTime.now());
        eventRepository.save(event);
    }
}
