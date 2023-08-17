package com.identa.catering.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.identa.catering.DummyObjects;
import com.identa.catering.converter.OrderConverter;
import com.identa.catering.entity.Order;
import com.identa.catering.model.OrdersResponseBody;
import com.identa.catering.service.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("dev")
class AdminControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private OrderService orderService;

    @Value(value = "${local.server.port}")
    private int port;

    @BeforeEach
    void setUp() {
        Order order = DummyObjects.getOrder();
        order.getOrderItems().get(0).setId(null);
        order.setId(null);
        order.setConfirmation(null);
        order.setStatus(null);
        orderService.save(OrderConverter.orderToDTO(order));
    }

    @Test
    void getNotConfirmedOrders() throws Exception {
        String urlNotConfirmedOrders = "/admin/notConfirmedOrders";
        ObjectMapper mapper = new ObjectMapper();
        ResultActions result = mockMvc.perform(get(urlNotConfirmedOrders)
                .contentType(MediaType.APPLICATION_JSON));
        MvcResult expect = result.andExpect(status().is2xxSuccessful()).andReturn();
        OrdersResponseBody response =  mapper.readValue(expect.getResponse().getContentAsString(), OrdersResponseBody.class);
        assertEquals(1, response.getResult().size());
        assertEquals("Success", response.getMsg());
    }

    @Test
    void approveOrder() throws Exception {
        performRequest("/admin/approveOrder");
    }

    @Test
    void declineOrder() throws Exception {
        performRequest("/admin/declineOrder");
    }

    @Test
    void done() throws Exception {
        performRequest("/admin/done");
    }

    private ResultActions performRequest(String urlTemplate) throws Exception {
        return mockMvc.perform(get("http://localhost:" + port + urlTemplate)
                        .contentType(APPLICATION_FORM_URLENCODED)
                        .param("id", "1"))
                .andExpect(status().is3xxRedirection());
    }
}