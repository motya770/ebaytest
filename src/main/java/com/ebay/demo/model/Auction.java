package com.ebay.demo.model;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
public class Auction {

    @Id
    @GeneratedValue
    private Long id;

    @Column
    @Temporal(TemporalType.TIMESTAMP)
    private Date fromTime;

    @Column
    @Temporal(TemporalType.TIMESTAMP)
    private Date toTime;

    @ManyToOne(fetch = FetchType.LAZY)
    private EbayItem ebayItem;
}



