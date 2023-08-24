package com.identa.catering.controller;

import com.identa.catering.entity.Order;
import com.identa.catering.model.dto.OrderDTO;
import com.identa.catering.service.OrderService;
import com.identa.catering.util.Util;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/main/approve")
public class ApproveController {

    private final OrderService orderService;
    private OrderDTO orderDTO;

    public ApproveController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    public String approveOrder(Model model,
                               HttpServletResponse response,
                               HttpServletRequest request) {
        orderDTO = Util.getOrderDTOFromCookie(request);
        if (orderDTO.getOrderItems().isEmpty())
            return Util.REDIRECT_TO_PRODUCTS;
        Order order = orderService.save(Util.getOrderDTOFromCookie(request));
        model.addAttribute("orderId", order.getId());
        orderDTO = Util.initOrder(response, orderDTO);
        return "message";
    }

}
