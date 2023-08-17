package com.identa.catering.model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Item {

    @NotNull(message = "Id can't be null!")
    @Min(value = 1, message = "Value can't be less than 1")
    private Long id;

}
