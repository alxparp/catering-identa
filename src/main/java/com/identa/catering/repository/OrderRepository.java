package com.identa.catering.repository;

import com.identa.catering.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByConfirmationNullOrderByIdDesc();
    List<Order> findByConfirmation_NameAndStatus_NameOrderByIdDesc(String confirmationName, String statusName);
    List<Order> findByStatus_NameOrderByIdDesc(String name);
}
