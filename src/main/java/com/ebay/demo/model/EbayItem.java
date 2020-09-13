package com.ebay.demo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.List;

@Entity
@Data
public class EbayItem {

    @Id
    private String id;

    //@Column
    //private String itemName;

    @JsonIgnore
    @OneToMany(mappedBy = "ebayItem", fetch = FetchType.LAZY)
    private List<Auction> auctions;
}
