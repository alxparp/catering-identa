package com.identa.catering.converter;

import com.identa.catering.entity.OrderItem;
import com.identa.catering.model.dto.OrderItemDTO;

public class OrderItemConverter {

    public static OrderItemDTO orderItemToDTO(OrderItem orderItem) {
        return OrderItemDTO.builder()
                .id(orderItem.getId())
                .quantity(orderItem.getQuantity())
                .productDTO(ProductConverter.productToDTO(orderItem.getProduct()))
                .build();
    }

    public static OrderItem DTOToOrderItem(OrderItemDTO orderItemDTO) {
        return OrderItem.builder()
                .id(orderItemDTO.getId())
                .quantity(orderItemDTO.getQuantity())
                .product(ProductConverter.DTOToProduct(orderItemDTO.getProductDTO()))
                .build();
    }

}
