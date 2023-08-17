package com.identa.catering.controller;

import com.identa.catering.model.*;
import com.identa.catering.model.dto.CategoryDTO;
import com.identa.catering.model.dto.OrderDTO;
import com.identa.catering.model.dto.OrderItemDTO;
import com.identa.catering.model.dto.ProductDTO;
import com.identa.catering.service.CategoryService;
import com.identa.catering.service.OrderItemService;
import com.identa.catering.service.OrderService;
import com.identa.catering.service.ProductService;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.annotation.SessionScope;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@SessionScope
@RequestMapping("/main")
public class MainController {

    private final CategoryService categoryService;
    private final ProductService productService;
    private final OrderItemService orderItemService;
    private final OrderService orderService;
    private OrderDTO orderDTO;
    private List<CategoryDTO> categories;
    private List<ProductDTO> products;
    private final String redirectProducts = "redirect:/main/products";

    public MainController(CategoryService categoryService,
                          ProductService productService,
                          OrderItemService orderItemService,
                          OrderService orderService) {
        this.categoryService = categoryService;
        this.productService = productService;
        this.orderItemService = orderItemService;
        this.orderService = orderService;
        initOrder();
    }

    @GetMapping("/products")
    public String products(@RequestParam(value = "id", required = false) Long id,
                           Model model) {

        CategoryDTO categoryDTO = (id == null || !categoryService.containsId(id)) ? categoryService.findFirst() : categoryService.findById(id);
        model.addAllAttributes(getSelectObjects(categoryDTO));
        return "products";
    }

    @GetMapping(value = "/displayCart")
    @ResponseBody
    public ResponseEntity<?> displayCart() {
        return getResult(new AjaxResponseBody());
    }

    @PostMapping(value = "/addProductToCart", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<?> addProduct(@RequestBody Item item, Errors errors) {
        return deleteOrAddProductToCart(item, errors, false);
    }

    @PostMapping(value = "/deleteFromCart", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<?> deleteFromCart(@RequestBody Item item, Errors errors) {
        return deleteOrAddProductToCart(item, errors, true);
    }

    @GetMapping("/checkout")
    public String checkout(Model model) {
        if (orderDTO.getOrderItems().isEmpty())
            return redirectProducts;
        model.addAttribute("order", orderDTO);
        return "checkout";
    }

    @GetMapping("/approve")
    public String approveOrder() {
        if (orderDTO.getOrderItems().isEmpty())
            return redirectProducts;
        orderService.save(orderDTO);
        initOrder();
        return "message";
    }

    private void initOrder() {
        orderDTO = new OrderDTO();
        orderDTO.setOrderItems(new ArrayList<>());
    }

    private Map<String, ?> getSelectObjects(CategoryDTO categoryDTO) {
        Map<String, Object> selectObjects = new HashMap<>();
        selectObjects.put("categories", categories = (categories == null) ? categoryService.findAll() : categories);
        selectObjects.put("products", categoryDTO != null ? productService.findByCategory(categoryDTO) : new ArrayList<>());
        selectObjects.put("order", orderDTO);
        return selectObjects;
    }

    private ResponseEntity<?> deleteOrAddProductToCart(Item item, Errors errors, boolean flag) {
        AjaxResponseBody result = new AjaxResponseBody();
        ResponseEntity<?> response = checkErrors(item, errors, result);
        if (response != null) return response;

        List<OrderItemDTO> orderItemDTOS = orderDTO.getOrderItems();
        ProductDTO productDTO = productService.findById(item.getId());


        if (flag) deleteOrderItem(orderItemDTOS, productDTO);
        else addOrderItem(orderItemDTOS, productDTO);

        orderDTO.setSum(orderItemService.calculateItemsSum(orderItemDTOS));

        return getResult(result);
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

    private ResponseEntity<?> getResult(AjaxResponseBody result) {
        result.setResult(orderDTO);
        result.setMsg("Success");
        return ResponseEntity.ok(result);
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

}
