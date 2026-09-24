package com.perproj.razorpay.common.config;

import com.perproj.razorpay.common.enums.EventAggregateType;
import jdk.jfr.Event;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
@ConfigurationProperties(prefix = "app.kafka")
public class KafkaProperties {

    private Map<String,String> topics = new HashMap<>();

    public String topicFor(EventAggregateType aggregateType){
        String topic = topics.get(aggregateType.name().toLowerCase());
        if(topic == null){
            throw new IllegalArgumentException("No Kafka topic found with aggregateType : "+ aggregateType);
        }
        return topic;
    }

}
