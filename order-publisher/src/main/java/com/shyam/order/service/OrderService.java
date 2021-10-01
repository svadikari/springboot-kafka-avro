package com.shyam.order.service;

import com.shyam.avro.order.Order;

/**
 * @author SVadikari on 2/21/21
 */

public interface OrderService {
    void order(Order orderEvent);
}
