package com.perproj.razorpay.payment.outbox;

import com.perproj.razorpay.common.config.KafkaProperties;

import com.perproj.razorpay.common.enums.OutboxStatus;
import com.perproj.razorpay.payment.entity.OutboxEvent;
import com.perproj.razorpay.payment.repository.OutboxEventRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class OutboxPoller {

    private final KafkaProperties kafkaProperties;
    private final OutboxEventRepository eventRepository;
    private final KafkaTemplate<String,Map<String,Object>> kafkaTemplate;
    private final OutboxResultHandler outboxResultHandler;

    @Scheduled(fixedDelay = 5000)
    public void poll(){

        List<OutboxEvent> pendingEvents = eventRepository.findByStatusOrderByCreatedAtAsc(OutboxStatus.PENDING);

        for (OutboxEvent event : pendingEvents){

           try{
               String topic = kafkaProperties.topicFor(event.getAggregateType());
               String key = extractMerchantId(event.getPayload());

               Map<String,Object> envelope = Map.of(
                       "eventType", event.getEventType(),
                       "aggregatorType", event.getAggregateType().name(),
                       "aggregateId" , event.getAggregateId().toString(),
                       "data" , event.getPayload()
               );

               kafkaTemplate.send(topic,key,envelope)
                       .get(5, TimeUnit.SECONDS);
               outboxResultHandler.handleEventPublished(event);
           }
           catch(Exception e){
               log.error("Outbox event failed eventId : {}, attempts : {}", event.getEventType(), event.getAttempt());
               outboxResultHandler.handleEventFailed(event, e.getMessage());
           }
        }
    }

    private String extractMerchantId(Map<String, Object> payload) {
        Object merchantId = payload.get("merchantId");
        return merchantId != null ? merchantId.toString() : "unknown";
    }

}
