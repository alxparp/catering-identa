package com.identa.catering.service;

import com.identa.catering.DummyObjects;
import com.identa.catering.converter.OrderConverter;
import com.identa.catering.converter.OrderItemConverter;
import com.identa.catering.entity.OrderItem;
import com.identa.catering.model.dto.OrderItemDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

class OrderItemServiceTest {

    private OrderItemService orderItemService;

    @Test
    void calculateItemsSum() {
        orderItemService = new OrderItemService();
        OrderItem orderItem = DummyObjects.getOrderItem();
        OrderItemDTO orderItemDTO = OrderItemConverter.orderItemToDTO(orderItem);

        double sumActual = orderItemService.calculateItemsSum(List.of(orderItemDTO));
        Assertions.assertEquals(orderItemDTO.getProductDTO().getPrice(), sumActual);

        sumActual = orderItemService.calculateItemsSum(new ArrayList<>());
        Assertions.assertEquals(0.0, sumActual);

        assertThrows(NullPointerException.class, () -> {
            orderItemService.calculateItemsSum(null);
        });

    }
}