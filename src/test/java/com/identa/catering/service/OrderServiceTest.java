package com.identa.catering.service;

import com.identa.catering.DummyObjects;
import com.identa.catering.converter.OrderConverter;
import com.identa.catering.entity.Order;
import com.identa.catering.entity.enums.ConfirmationType;
import com.identa.catering.entity.enums.StatusType;
import com.identa.catering.model.dto.CategoryDTO;
import com.identa.catering.model.dto.OrderDTO;
import com.identa.catering.repository.OrderRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;
    @Mock
    private ConfirmationService confirmationService;
    @Mock
    private StatusService statusService;
    private OrderService orderService;
    private Order order;
    private OrderDTO orderDTOExpected;

    @BeforeEach
    void setUp() {
        orderService = new OrderService(orderRepository, confirmationService, statusService);
        order = DummyObjects.getOrder();
        orderDTOExpected = OrderConverter.orderToDTO(order);
    }

    @Test
    void save() {
        when(confirmationService.findByType(ConfirmationType.valueOf(order.getConfirmation().getName()))).thenReturn(order.getConfirmation());
        when(statusService.findByType(StatusType.valueOf(order.getStatus().getName()))).thenReturn(order.getStatus());
        when(orderRepository.save(order)).thenReturn(order);

        Order orderActual = orderService.save(orderDTOExpected);

        Assertions.assertEquals(order, orderActual);
    }

    @Test
    void findNotConfirmed() {
        when(orderRepository.findByConfirmationNullOrderByIdDesc()).thenReturn(List.of(order));

        List<OrderDTO> orderDTOSActual = orderService.findNotConfirmed();

        Assertions.assertEquals(List.of(orderDTOExpected), orderDTOSActual);
    }

    @Test
    void findConfirmed() {
        when(orderRepository.findByConfirmation_NameAndStatus_NameOrderByIdDesc(anyString(), anyString())).thenReturn(List.of(order));

        List<OrderDTO> orderDTOSActual = orderService.findConfirmed();

        Assertions.assertEquals(List.of(orderDTOExpected), orderDTOSActual);
    }

    @Test
    void findDone() {
        when(orderRepository.findByStatus_NameOrderByIdDesc(anyString())).thenReturn(List.of(order));

        List<OrderDTO> orderDTOSActual = orderService.findDone();

        Assertions.assertEquals(List.of(orderDTOExpected), orderDTOSActual);
    }

    @Test
    void findById() {
        when(orderRepository.findById(order.getId())).thenReturn(Optional.of(order));

        OrderDTO orderDTOActual = orderService.findById(order.getId());

        Assertions.assertEquals(orderDTOExpected, orderDTOActual);
        assertThrows(NoSuchElementException.class, () -> {
            orderService.findById(2L);
        });
    }

    @Test
    void containsId() {
        when(orderRepository.existsById(order.getId())).thenReturn(true);

        boolean containsActual = orderService.containsId(order.getId());

        Assertions.assertTrue(containsActual);
    }
}