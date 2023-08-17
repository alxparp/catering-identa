package com.identa.catering.model;

import com.identa.catering.model.dto.OrderDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AjaxResponseBody {

    private String msg;

    private OrderDTO result;

}
