package com.shyam.order.clients;

import com.shyam.avro.order.Order;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;

/**
 * @author SVadikari on 2/21/21
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class KafkaProducer {

    @Value("${kafka.properties.order-topic}")
    private String orderTopic;

    private final KafkaTemplate<String, Order> kafkaTemplate;

    @Retryable(maxAttemptsExpression = "${kafka.properties.max_count}",
            backoff = @Backoff(delayExpression = "${kafka.properties.max_delay}"))
    public void produce(Order orderEvent) {
        kafkaTemplate.send(orderTopic, orderEvent.getOrderNumber(), orderEvent).addCallback(
                result -> {
                    final RecordMetadata m;
                    if (result != null) {
                        m = result.getRecordMetadata();
                        log.info("Produced record to topic {} partition {} @ offset {}",
                                m.topic(),
                                m.partition(),
                                m.offset());
                    }
                },
                exception -> log.error("Failed to produce to kafka", exception));
        kafkaTemplate.flush();
    }
}
