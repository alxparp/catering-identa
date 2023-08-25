package com.identa.catering.converter;

import com.identa.catering.entity.Product;
import com.identa.catering.model.dto.ProductDTO;

public class ProductConverter {

    public static ProductDTO productToDTO(Product product) {
        return ProductDTO.builder()
                .id(product.getId())
                .name(product.getName())
                .price(product.getPrice())
                .img(product.getImg())
                .category(CategoryConverter.categoryToDTO(product.getCategory()))
                .build();
    }

    public static Product DTOToProduct(ProductDTO productDTO) {
        return Product.builder()
                .id(productDTO.getId())
                .name(productDTO.getName())
                .price(productDTO.getPrice())
                .img(productDTO.getImg())
                .category(CategoryConverter.DTOToCategory(productDTO.getCategory()))
                .build();
    }

}
