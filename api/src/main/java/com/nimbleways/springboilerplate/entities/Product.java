package com.nimbleways.springboilerplate.entities;

import lombok.*;

import java.time.LocalDate;

import javax.persistence.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "lead_time")
    private Integer leadTime;

    @Column(name = "available")
    private Integer available;

    @Column(name = "type")
    private String type;

    @Column(name = "name")
    private String name;

    @Column(name = "expiry_date")
    private LocalDate expiryDate;

    @Column(name = "season_start_date")
    private LocalDate seasonStartDate;

    @Column(name = "season_end_date")
    private LocalDate seasonEndDate;

    @Column(name = "start_sell_on")
    private LocalDate startSellOn;

    //période de temps très limitée
    @Column(name = "period")
    private Integer period;

    @Column(name = "had_selled")
    private Integer hadSelled;
    //quantité maximale d'articles pouvant être vendus, FlashSale
    @Column(name = "quantity_max_to_sell")
    private Integer quantityMaxToSell;
}
