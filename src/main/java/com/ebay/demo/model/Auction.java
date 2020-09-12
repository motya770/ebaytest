package com.ebay.demo.model;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
public class Auction {

    @Id
    @GeneratedValue
    private Long id;

    @Column
    private LocalDateTime fromTime;

    @Column
    private LocalDateTime toTime;

    @ManyToOne(fetch = FetchType.LAZY)
    private EbayItem ebayItem;
}



