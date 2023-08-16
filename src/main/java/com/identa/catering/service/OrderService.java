package com.identa.catering.service;

import com.identa.catering.converter.OrderConverter;
import com.identa.catering.entity.Order;
import com.identa.catering.model.dto.OrderDTO;
import com.identa.catering.repository.OrderRepository;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

import static com.identa.catering.entity.enums.ConfirmationType.CONFIRMED;
import static com.identa.catering.entity.enums.StatusType.DONE;
import static com.identa.catering.entity.enums.StatusType.IN_PROCESS;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final ConfirmationService confirmationService;
    private final StatusService statusService;

    public OrderService(OrderRepository orderRepository,
                        ConfirmationService confirmationService,
                        StatusService statusService) {
        this.orderRepository = orderRepository;
        this.confirmationService = confirmationService;
        this.statusService = statusService;
    }

    public void save(OrderDTO orderDTO) {
        Order order = OrderConverter.DTOToOrder(orderDTO);
        order.setCreated(Date.valueOf(LocalDate.now()));
        if (orderDTO.getConfirmation() != null)
            order.setConfirmation(confirmationService.findByType(orderDTO.getConfirmation()));
        if (orderDTO.getStatus() != null)
            order.setStatus(statusService.findByType(orderDTO.getStatus()));
        orderRepository.save(order);
    }

    public List<OrderDTO> findNotConfirmed() {
        return orderRepository.findByConfirmationNull()
                .stream()
                .map(OrderConverter::orderToDTO)
                .toList();
    }

    public List<OrderDTO> findConfirmed() {
        return orderRepository.findByConfirmation_NameAndStatus_Name(CONFIRMED.name(), IN_PROCESS.name())
                .stream()
                .map(OrderConverter::orderToDTO)
                .toList();
    }

    public List<OrderDTO> findDone() {
        return orderRepository.findByStatus_Name(DONE.name())
                .stream()
                .map(OrderConverter::orderToDTO)
                .toList();
    }

    public OrderDTO findById(Long id) {
        return OrderConverter.orderToDTO(orderRepository.findById(id).orElseThrow());
    }

    public void update(OrderDTO orderDTO) {
        save(orderDTO);
    }
}
