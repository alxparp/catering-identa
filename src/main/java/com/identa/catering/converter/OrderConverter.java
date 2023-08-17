package com.identa.catering.converter;

import com.identa.catering.entity.Order;
import com.identa.catering.entity.enums.ConfirmationType;
import com.identa.catering.entity.enums.StatusType;
import com.identa.catering.model.dto.OrderDTO;

public class OrderConverter {

    public static OrderDTO orderToDTO(Order order) {
        return OrderDTO.builder()
                .id(order.getId())
                .created(order.getCreated())
                .sum(order.getSum())
                .orderItems(order.getOrderItems()
                        .stream()
                        .map(OrderItemConverter::orderItemToDTO)
                        .toList())
                .confirmation(order.getConfirmation() != null ?
                        ConfirmationType.valueOf(order.getConfirmation().getName()) :
                        null)
                .status(order.getStatus() != null ?
                        StatusType.valueOf(order.getStatus().getName()) :
                        null)
                .build();
    }

    public static Order DTOToOrder(OrderDTO orderDTO) {
        return Order.builder()
                .id(orderDTO.getId())
                .created(orderDTO.getCreated())
                .sum(orderDTO.getSum())
                .orderItems(orderDTO.getOrderItems()
                        .stream()
                        .map(OrderItemConverter::DTOToOrderItem)
                        .toList())
                .build();
    }

}
