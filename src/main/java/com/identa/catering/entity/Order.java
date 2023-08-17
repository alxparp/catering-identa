package com.identa.catering.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "order_t")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Long id;
    @OneToMany(cascade = CascadeType.PERSIST)
    @JoinColumn(name="order_id")
    private List<OrderItem> orderItems;
    private Date created;
    private Double sum;
    @ManyToOne
    @JoinColumn(name = "confirmation_id")
    private Confirmation confirmation;
    @ManyToOne
    @JoinColumn(name = "status_id")
    private Status status;


}
