package com.identa.catering;

import com.identa.catering.entity.*;
import com.identa.catering.model.Item;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

import static com.identa.catering.entity.enums.ConfirmationType.CONFIRMED;
import static com.identa.catering.entity.enums.StatusType.DONE;

public class DummyObjects {

    public static Category getCategory() {
        return Category.builder()
                .id(1L)
                .name("Десерт")
                .build();
    }

    public static Confirmation getConfirmation() {
        return Confirmation.builder()
                .id(1L)
                .name(CONFIRMED.name())
                .build();
    }

    public static Product getProduct() {
        return Product.builder()
                .id(1L)
                .name("Борщ зі сметаною")
                .img("borsh.jpg")
                .price(121.50)
                .category(getCategory())
                .build();

    }

    public static OrderItem getOrderItem() {
        return OrderItem.builder()
                .id(1L)
                .product(getProduct())
                .quantity(1)
                .build();
    }

    public static Status getStatus() {
        return Status.builder()
                .id(1L)
                .name(DONE.name())
                .build();
    }

    public static Order getOrder() {
        return Order.builder()
                .id(1L)
                .orderItems(List.of(getOrderItem()))
                .created(Date.valueOf(LocalDate.now()))
                .sum(getProduct().getPrice())
                .confirmation(getConfirmation())
                .status(getStatus())
                .build();
    }

    public static Item getItem() {
        return new Item(1L);
    }

}
