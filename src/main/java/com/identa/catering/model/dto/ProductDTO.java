package com.identa.catering.model.dto;

import com.identa.catering.entity.Category;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductDTO implements Serializable {

    private Long id;
    private String name;
    private Double price;
    private String img;
    private CategoryDTO category;

}
