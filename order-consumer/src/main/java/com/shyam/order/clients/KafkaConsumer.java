package com.shyam.order.clients;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.shyam.avro.order.Order;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.io.FileWriter;
import java.io.IOException;

/**
 * @author SVadikari on 07/29/21
 */
@Slf4j
@Component
@AllArgsConstructor
public class KafkaConsumer {

    @KafkaListener(topics = "#{'${kafka.properties.topic}'}", groupId = "${kafka.properties.consumer.group-id}")
    public void consume(final ConsumerRecord<String, Order> consumerRecord) {
        log.info("received {} ", consumerRecord.key());
        try {
            FileWriter fileWriter = new FileWriter(consumerRecord.key().concat("_response.json"));
            fileWriter.write(consumerRecord.value().toString());
            fileWriter.flush();
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    abstract class IgnoreSchemaProperty {
        @JsonIgnore
        abstract void getSchema();
    }
}
