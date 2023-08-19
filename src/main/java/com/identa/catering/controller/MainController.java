package com.identa.catering.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.identa.catering.entity.Order;
import com.identa.catering.model.*;
import com.identa.catering.model.dto.CategoryDTO;
import com.identa.catering.model.dto.OrderDTO;
import com.identa.catering.model.dto.OrderItemDTO;
import com.identa.catering.model.dto.ProductDTO;
import com.identa.catering.service.CategoryService;
import com.identa.catering.service.OrderItemService;
import com.identa.catering.service.OrderService;
import com.identa.catering.service.ProductService;
import com.identa.catering.util.Util;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.annotation.SessionScope;
import org.springframework.web.util.WebUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

import static java.nio.charset.StandardCharsets.*;

@Controller
@RequestMapping("/main")
public class MainController {

    private final CategoryService categoryService;
    private final ProductService productService;
    private final OrderItemService orderItemService;
    private OrderDTO orderDTO;
    private List<CategoryDTO> categories;

    public MainController(CategoryService categoryService,
                          ProductService productService,
                          OrderItemService orderItemService) {
        this.categoryService = categoryService;
        this.productService = productService;
        this.orderItemService = orderItemService;
    }

    @GetMapping("/products")
    public String products(@RequestParam(value = "id", required = false) Long id,
                           Model model,
                           HttpServletResponse response) {

        CategoryDTO categoryDTO = (id == null || !categoryService.containsId(id))
                ? categoryService.findFirst()
                : categoryService.findById(id);
        model.addAllAttributes(getSelectObjects(categoryDTO));
        orderDTO = (orderDTO == null) ? Util.initOrder(response, orderDTO) : orderDTO;
        return "products";
    }

    private Map<String, ?> getSelectObjects(CategoryDTO categoryDTO) {
        Map<String, Object> selectObjects = new HashMap<>();
        selectObjects.put("categories", categories = (categories == null)
                ? categoryService.findAll()
                : categories);
        selectObjects.put("products", categoryDTO != null
                ? productService.findByCategory(categoryDTO)
                : new ArrayList<>());
        return selectObjects;
    }

    @GetMapping(value = "/displayCart")
    @ResponseBody
    public ResponseEntity<?> displayCart(HttpServletResponse response) {
        return getResult(new AjaxResponseBody(), response);
    }

    @PostMapping(value = "/addProductToCart", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<?> addProduct(@RequestBody Item item,
                                        Errors errors,
                                        HttpServletRequest request,
                                        HttpServletResponse response) {
        return deleteOrAddProductToCart(item, errors, request, response, false);
    }

    @PostMapping(value = "/deleteFromCart", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<?> deleteFromCart(@RequestBody Item item,
                                            Errors errors,
                                            HttpServletRequest request,
                                            HttpServletResponse response) {
        return deleteOrAddProductToCart(item, errors, request, response, true);
    }

    private ResponseEntity<?> deleteOrAddProductToCart(Item item,
                                                       Errors errors,
                                                       HttpServletRequest request,
                                                       HttpServletResponse response,
                                                       boolean flag) {
        orderDTO = Util.getOrderDTOFromCookie(request);
        AjaxResponseBody result = new AjaxResponseBody();
        ResponseEntity<?> responseEntity = checkErrors(item, errors, result);
        if (responseEntity != null) return responseEntity;

        List<OrderItemDTO> orderItemDTOS = orderDTO.getOrderItems();
        ProductDTO productDTO = productService.findById(item.getId());

        if (flag) deleteOrderItem(orderItemDTOS, productDTO);
        else addOrderItem(orderItemDTOS, productDTO);

        orderDTO.setSum(orderItemService.calculateItemsSum(orderItemDTOS));

        return getResult(result, response);
    }

    private ResponseEntity<?> checkErrors(Item item,
                                          Errors errors,
                                          AjaxResponseBody result) {
        if (errors.hasErrors()) {
            result.setMsg(errors.getAllErrors()
                    .stream().map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.joining(",")));

            return ResponseEntity.badRequest().body(result);
        }

        if (!productService.containsId(item.getId())) {
            result.setMsg("Such product doesn't exist");
            return ResponseEntity.badRequest().body(result);
        }

        return null;
    }

    private void addOrderItem(List<OrderItemDTO> orderItemDTOS, ProductDTO productDTO) {
        for (OrderItemDTO orderItemDTO : orderItemDTOS) {
            if (orderItemDTO.getProductDTO().equals(productDTO)) {
                orderItemDTO.setQuantity(orderItemDTO.getQuantity() + 1);
                return;
            }
        }
        OrderItemDTO orderItemDTO = OrderItemDTO.builder()
                .quantity(1)
                .productDTO(productDTO)
                .build();
        orderItemDTOS.add(orderItemDTO);
    }

    private void deleteOrderItem(List<OrderItemDTO> orderItemDTOS, ProductDTO productDTO) {
        for (OrderItemDTO orderItemDTO : orderItemDTOS) {
            if (orderItemDTO.getProductDTO().equals(productDTO)) {
                if (orderItemDTO.getQuantity() == 1) orderItemDTOS.remove(orderItemDTO);
                orderItemDTO.setQuantity(orderItemDTO.getQuantity() - 1);
                return;
            }
        }
    }

    private ResponseEntity<?> getResult(AjaxResponseBody result, HttpServletResponse response) {
        Util.saveOrderDTOAsCookie(response, orderDTO);
        result.setResult(orderDTO);
        result.setMsg("Success");
        return ResponseEntity.ok(result);
    }


}
