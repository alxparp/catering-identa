package com.identa.catering.model.dto;

import com.identa.catering.entity.enums.ConfirmationType;
import com.identa.catering.entity.enums.StatusType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderDTO implements Serializable {

    private Long id;
    private List<OrderItemDTO> orderItems;
    private Date created;
    private Double sum;
    private ConfirmationType confirmation;
    private StatusType status;
}
