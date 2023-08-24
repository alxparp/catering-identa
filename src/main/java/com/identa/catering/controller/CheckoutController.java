package com.identa.catering.controller;

import com.identa.catering.model.dto.OrderDTO;
import com.identa.catering.util.Util;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/main/checkout")
public class CheckoutController {

    private OrderDTO orderDTO;

    @GetMapping
    public String checkout(Model model, HttpServletRequest request) {
        orderDTO = Util.getOrderDTOFromCookie(request);
        if (orderDTO != null && orderDTO.getOrderItems().isEmpty())
            return Util.REDIRECT_TO_PRODUCTS;
        model.addAttribute("order", orderDTO);
        return "checkout";
    }

}
