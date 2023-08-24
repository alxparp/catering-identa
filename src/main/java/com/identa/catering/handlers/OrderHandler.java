package com.identa.catering.handlers;

import com.identa.catering.entity.Order;
import com.identa.catering.model.dto.CategoryDTO;
import com.identa.catering.model.dto.OrderDTO;
import com.identa.catering.model.dto.OrderItemDTO;
import com.identa.catering.model.dto.ProductDTO;
import com.identa.catering.service.CategoryService;
import com.identa.catering.service.OrderItemService;
import com.identa.catering.service.OrderService;
import com.identa.catering.service.ProductService;
import org.springframework.binding.message.MessageBuilder;
import org.springframework.binding.message.MessageContext;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class OrderHandler {
    private final CategoryService categoryService;
    private final ProductService productService;
    private final OrderItemService orderItemService;
    private final OrderService orderService;

    public OrderHandler(CategoryService categoryService,
                        ProductService productService,
                        OrderItemService orderItemService,
                        OrderService orderService) {
        this.categoryService = categoryService;
        this.productService = productService;
        this.orderItemService = orderItemService;
        this.orderService = orderService;
    }

    public OrderDTO init() {
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setOrderItems(new ArrayList<>());
        return orderDTO;
    }

    public CategoryDTO initCategory() {
        return categoryService.findFirst();
    }

    public List<ProductDTO> getProducts(Long categoryId, CategoryDTO globalCategory) {

        if (categoryId == null) categoryId = globalCategory.getId();

        CategoryDTO categoryDTO = (categoryId == null || !categoryService.containsId(categoryId))
                ? categoryService.findFirst()
                : categoryService.findById(categoryId);

        List<ProductDTO> products = categoryDTO != null
                ? productService.findByCategory(categoryDTO)
                : new ArrayList<>();

        globalCategory.setId(categoryId);

        return products;
    }

    public String deleteProduct(OrderDTO orderDTO, Long productId, MessageContext context) {
        return deleteOrAddProductToCart(orderDTO, productId, true, context);
    }

    public String addProduct(OrderDTO orderDTO, Long productId, MessageContext context) {
        return deleteOrAddProductToCart(orderDTO, productId, false, context);
    }

    public String approveOrder(OrderDTO orderDTO) {
        if (orderDTO.getOrderItems().isEmpty())
            return "personal";
        Order order = orderService.save(orderDTO);
        orderDTO.setId(order.getId());
        return "message";
    }

    public String checkout(OrderDTO orderDTO, MessageContext error) {
        if (orderDTO.getOrderItems().isEmpty()) {
            setErrorMessage(error);
            return "failure";
        }
        return "success";
    }

    private String deleteOrAddProductToCart(OrderDTO orderDTO, Long productId, boolean flag, MessageContext error) {
        String transitionValue = "success";

        if (productId == null || !productService.containsId(productId)) {
            setErrorMessage(error);
            return "failure";
        }

        List<OrderItemDTO> orderItemDTOS = orderDTO.getOrderItems();
        if (orderItemDTOS == null) {
            orderDTO.setOrderItems(new ArrayList<>());
            orderItemDTOS = orderDTO.getOrderItems();
        }
        ProductDTO productDTO = productService.findById(productId);

        if (flag) deleteOrderItem(orderItemDTOS, productDTO);
        else addOrderItem(orderItemDTOS, productDTO);

        orderDTO.setSum(orderItemService.calculateItemsSum(orderItemDTOS));

        return transitionValue;
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

    private void setErrorMessage(MessageContext error) {
        error.addMessage(new MessageBuilder(). //
                error() //
                .source("productExist") //
                .defaultText("Such product doesn't exist or no one product was added to the cart") //
                .build());
    }

}
