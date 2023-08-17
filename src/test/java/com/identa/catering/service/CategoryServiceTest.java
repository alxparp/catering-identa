package com.identa.catering.service;

import com.identa.catering.DummyObjects;
import com.identa.catering.converter.CategoryConverter;
import com.identa.catering.entity.Category;
import com.identa.catering.model.dto.CategoryDTO;
import com.identa.catering.repository.CategoryRepository;
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
class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;
    private CategoryService categoryService;
    private Category category;
    private CategoryDTO categoryDTOExpected;

    @BeforeEach
    void setUp() {
        categoryService = new CategoryService(categoryRepository);
        category = DummyObjects.getCategory();
        categoryDTOExpected = CategoryConverter.categoryToDTO(category);
    }

    @Test
    void findAll() {
        List<CategoryDTO> categoryDTOSExpected = List.of(categoryDTOExpected);
        when(categoryRepository.findAll()).thenReturn(List.of(category));

        List<CategoryDTO> categoryDTOSActual = categoryService.findAll();

        Assertions.assertEquals(categoryDTOSExpected, categoryDTOSActual);

    }

    @Test
    void findById() {
        when(categoryRepository.findById(category.getId())).thenReturn(Optional.of(category));

        CategoryDTO categoryDTOActual = categoryService.findById(category.getId());

        Assertions.assertEquals(categoryDTOExpected, categoryDTOActual);
        assertThrows(NoSuchElementException.class, () -> {
            categoryService.findById(2L);
        });

    }

    @Test
    void findFirst() {
        when(categoryRepository.findFirstByOrderById()).thenReturn(Optional.of(category));

        CategoryDTO categoryDTOActual = categoryService.findFirst();

        Assertions.assertEquals(categoryDTOExpected, categoryDTOActual);
        assertThrows(NoSuchElementException.class, () -> {
            categoryService.findById(2L);
        });
    }

    @Test
    void containsId() {
        when(categoryRepository.existsById(category.getId())).thenReturn(true);

        boolean containsActual = categoryService.containsId(category.getId());

        Assertions.assertTrue(containsActual);
    }
}