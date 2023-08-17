package com.identa.catering.service;

import com.identa.catering.converter.CategoryConverter;
import com.identa.catering.model.dto.CategoryDTO;
import com.identa.catering.repository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public List<CategoryDTO> findAll() {
        return categoryRepository.findAll()
                .stream()
                .map(CategoryConverter::categoryToDTO)
                .toList();
    }

    public CategoryDTO findById(Long id) {
        return CategoryConverter.categoryToDTO(categoryRepository.findById(id).orElseThrow());
    }

    public CategoryDTO findFirst() {
        return CategoryConverter.categoryToDTO(categoryRepository.findFirstByOrderById().orElseThrow());
    }

    public boolean containsId(Long id) {
        return categoryRepository.existsById(id);
    }
}
