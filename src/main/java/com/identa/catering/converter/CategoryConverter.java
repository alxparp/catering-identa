package com.identa.catering.converter;

import com.identa.catering.entity.Category;
import com.identa.catering.model.dto.CategoryDTO;

public class CategoryConverter {

    public static CategoryDTO categoryToDTO(Category category) {
        return CategoryDTO.builder()
                .id(category.getId())
                .name(category.getName())
                .build();
    }

    public static Category DTOToCategory(CategoryDTO categoryDTO) {
        return Category.builder()
                .id(categoryDTO.getId())
                .name(categoryDTO.getName())
                .build();
    }

}
