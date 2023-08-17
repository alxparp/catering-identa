package com.identa.catering.service;

import com.identa.catering.DummyObjects;
import com.identa.catering.converter.CategoryConverter;
import com.identa.catering.converter.ProductConverter;
import com.identa.catering.entity.Confirmation;
import com.identa.catering.entity.Product;
import com.identa.catering.entity.enums.ConfirmationType;
import com.identa.catering.model.dto.OrderDTO;
import com.identa.catering.model.dto.ProductDTO;
import com.identa.catering.repository.ProductRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;
    private ProductService productService;
    private Product product;
    private ProductDTO productDTOExpected;

    @BeforeEach
    void setUp() {
        productService = new ProductService(productRepository);
        product = DummyObjects.getProduct();
        productDTOExpected = ProductConverter.productToDTO(product);
    }

    @Test
    void findById() {
        when(productRepository.findById(product.getId())).thenReturn(Optional.of(product));

        ProductDTO productDTOActual = productService.findById(product.getId());

        Assertions.assertEquals(productDTOExpected, productDTOActual);
        assertThrows(NoSuchElementException.class, () -> {
            productService.findById(2L);
        });
    }

    @Test
    void containsId() {
        when(productRepository.existsById(product.getId())).thenReturn(true);

        boolean containsActual = productService.containsId(product.getId());

        Assertions.assertTrue(containsActual);
    }

    @Test
    void findByCategory() {
        when(productRepository.findByCategory(product.getCategory())).thenReturn(List.of(product));

        List<ProductDTO> productDTOSActual = productService.findByCategory(CategoryConverter.categoryToDTO(product.getCategory()));

        Assertions.assertEquals(List.of(productDTOExpected), productDTOSActual);
    }
}