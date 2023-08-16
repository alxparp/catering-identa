package com.identa.catering.service;

import com.identa.catering.model.dto.OrderItemDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderItemService {
    public double calculateItemsSum(List<OrderItemDTO> orderItemDTOS) {
        double sum = 0.0;
        for (OrderItemDTO item: orderItemDTOS) {
            sum += item.getProductDTO().getPrice()*item.getQuantity();
        }
        return sum;
    }
}
