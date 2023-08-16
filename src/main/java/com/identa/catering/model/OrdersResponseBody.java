package com.identa.catering.model;

import com.identa.catering.model.dto.OrderDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrdersResponseBody {

    private String msg;

    private List<OrderDTO> result;

}
