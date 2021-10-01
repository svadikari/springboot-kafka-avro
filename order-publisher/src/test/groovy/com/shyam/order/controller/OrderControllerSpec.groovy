package com.shyam.order.controller

import com.shyam.avro.order.Order
import com.shyam.order.service.OrderService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import spock.lang.Specification

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

/**
 * @author SVadikari on 2/26/21
 */
class OrderControllerSpec extends Specification {
    @Autowired
    def service = Mock(OrderService)
    def controller = new OrderController()
    MockMvc mockMvc = MockMvcBuilders.standaloneSetup(controller).build()

    def request;

    def setup() {
        request = new Order();
    }

    def "Validate create PO event save functionality" (){
        given: 'Order event request'
        service.order(_ as Order) >> void
        expect: 'Status is 400 with failure response'
        mockMvc.perform(post("/"))
        .andExpect(status().isBadRequest())
        .andReturn()
    }
    def "SayHello should return 'Hello' Message!"() {
        expect: 'Status is 200 with Hello Shyam'
        mockMvc.perform(get(url))
                .andExpect(status().isOk())
                .andReturn()
                .response
                .contentAsString == respMessage
        where:
        url             || respMessage
        '/?user=Shyam' || 'Hello Shyam'
    }
}
