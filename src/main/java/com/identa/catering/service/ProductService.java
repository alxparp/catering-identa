package com.identa.catering.service;

import com.identa.catering.converter.CategoryConverter;
import com.identa.catering.converter.ProductConverter;
import com.identa.catering.model.dto.CategoryDTO;
import com.identa.catering.model.dto.ProductDTO;
import com.identa.catering.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public ProductDTO findById(Long id) {
        return ProductConverter.productToDTO(
                productRepository.findById(id).orElseThrow());
    }

    public boolean containsId(Long id) {
        return productRepository.existsById(id);
    }

    public List<ProductDTO> findByCategory(CategoryDTO category) {
        return productRepository.findByCategory(
                CategoryConverter.DTOToCategory(category))
                .stream()
                .map(ProductConverter::productToDTO)
                .toList();
    }
}
