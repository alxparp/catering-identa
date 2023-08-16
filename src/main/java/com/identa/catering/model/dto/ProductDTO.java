package com.identa.catering.model.dto;

import com.identa.catering.entity.Category;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductDTO {

    private Long id;
    private String name;
    private Double price;
    private String img;
    private Category category;

}
