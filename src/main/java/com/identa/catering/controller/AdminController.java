package com.identa.catering.controller;

import com.identa.catering.entity.enums.ConfirmationType;
import com.identa.catering.model.OrdersResponseBody;
import com.identa.catering.model.dto.OrderDTO;
import com.identa.catering.service.OrderService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

import static com.identa.catering.entity.enums.ConfirmationType.CONFIRMED;
import static com.identa.catering.entity.enums.ConfirmationType.DECLINED;
import static com.identa.catering.entity.enums.StatusType.DONE;
import static com.identa.catering.entity.enums.StatusType.IN_PROCESS;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final OrderService orderService;
    private final String redirectAdmin = "redirect:/admin";

    public AdminController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    public String admin(Model model) {
        model.addAllAttributes(getSelectObjects());
        return "admin";
    }

    @GetMapping(value = "/notConfirmedOrders", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<?> getNotConfirmedOrders() {
        OrdersResponseBody response = new OrdersResponseBody();
        response.setMsg("Success");
        response.setResult(orderService.findNotConfirmed());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/approveOrder")
    public String approveOrder(@RequestParam(value = "id", required = false) Long id,
                               Model model) {
        return handleAndGoToAdmin(CONFIRMED, model, id);
    }

    @GetMapping("/declineOrder")
    public String declineOrder(@RequestParam(value = "id", required = false) Long id,
                               Model model) {
        return handleAndGoToAdmin(DECLINED, model, id);
    }

    @GetMapping("/done")
    public String done(@RequestParam(value = "id", required = false) Long id,
                               Model model) {
        if(!isValidId(id)) return redirectAdmin;
        OrderDTO orderDTO = orderService.findById(id);
        orderDTO.setStatus(DONE);

        return updateAndGoToAdmin(orderDTO, model);
    }

    private boolean isValidId(Long id) {
        if (id == null || id < 1 || !orderService.containsId(id)) return false;
        return true;
    }

    private String handleAndGoToAdmin(ConfirmationType confirmationType, Model model, Long id) {
        if(!isValidId(id)) return redirectAdmin;
        OrderDTO orderDTO = orderService.findById(id);
        orderDTO.setConfirmation(confirmationType);
        orderDTO.setStatus(IN_PROCESS);

        return updateAndGoToAdmin(orderDTO, model);
    }

    private String updateAndGoToAdmin(OrderDTO orderDTO, Model model) {
        orderService.update(orderDTO);
        model.addAllAttributes(getSelectObjects());

        return redirectAdmin;
    }

    private Map<String, ?> getSelectObjects() {
        Map<String, Object> selectObjects = new HashMap<>();
        selectObjects.put("ordersConfirmed", orderService.findConfirmed());
        selectObjects.put("ordersDone", orderService.findDone());
        return selectObjects;
    }
}
