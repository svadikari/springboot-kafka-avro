package com.shyam.order.service.impl;


import com.shyam.avro.order.Order;
import com.shyam.order.clients.KafkaProducer;
import com.shyam.order.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author SVadikari on 2/21/21
 */

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private KafkaProducer kafkaProducer;

    @Override
    public void order(Order orderEvent) {
        kafkaProducer.produce(orderEvent);
    }
}
