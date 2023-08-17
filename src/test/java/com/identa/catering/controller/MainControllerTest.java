package com.identa.catering.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.identa.catering.DummyObjects;
import com.identa.catering.model.AjaxResponseBody;
import com.identa.catering.model.Item;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("dev")
class MainControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Value(value = "${local.server.port}")
    private int port;

    @Test
    void products() throws Exception {
        performRequest("/main/products");
    }

    private ResultActions performRequest(String urlTemplate) throws Exception {
        return mockMvc.perform(get("http://localhost:" + port + urlTemplate)
                        .contentType(APPLICATION_FORM_URLENCODED)
                        .param("id", "1"))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    void displayCart() throws Exception {
        String urlDisplayCart = "/main/displayCart";
        ResultActions result = mockMvc.perform(get(urlDisplayCart)
                .contentType(MediaType.APPLICATION_JSON));
        result.andExpect(status().is2xxSuccessful());
    }

    @Test
    void addProduct() throws Exception {
        String urlAddProduct = "/main/addProductToCart";

        AjaxResponseBody response = getSuccessfulResponse(urlAddProduct);
        assertEquals(response.getResult().getSum(), 210.00);
        assertEquals(response.getResult().getOrderItems().get(0).getQuantity(), 1);

        Item item = DummyObjects.getItem();
        ObjectMapper mapper = new ObjectMapper();

        item.setId(null);
        mapper.writeValueAsString(item);
        ResultActions result = performRequest(mapper.writeValueAsString(item), urlAddProduct);
        result.andExpect(status().is4xxClientError());

        item.setId(20L);
        mapper.writeValueAsString(item);
        result = performRequest(mapper.writeValueAsString(item), urlAddProduct);
        result.andExpect(status().is4xxClientError());
    }

    @Test
    void deleteFromCart() throws Exception {
        String urlDeleteProduct = "/main/deleteFromCart";

        AjaxResponseBody response = getSuccessfulResponse(urlDeleteProduct);
        assertEquals(response.getResult().getSum(), 0.00);
        assertEquals(response.getResult().getOrderItems().size(), 0);
    }

    private AjaxResponseBody getSuccessfulResponse(String urlTemplate) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        Item item = DummyObjects.getItem();
        mapper.writeValueAsString(item);
        ResultActions result = performRequest(mapper.writeValueAsString(item), urlTemplate);
        MvcResult expect = result.andExpect(status().is2xxSuccessful()).andReturn();
        return mapper.readValue(expect.getResponse().getContentAsString(), AjaxResponseBody.class);
    }

    private ResultActions performRequest(String request, String urlTemplate) throws Exception {
        return mockMvc.perform(post(urlTemplate)
                .contentType(MediaType.APPLICATION_JSON)
                .content(request));
    }
}