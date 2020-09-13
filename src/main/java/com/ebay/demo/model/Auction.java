package com.ebay.demo.model;

import com.ebay.demo.utils.Utils;
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
    private Long fromTime;

    @Column
    private Long toTime;

    @ManyToOne(fetch = FetchType.LAZY)
    private EbayItem ebayItem;

    public LocalDateTime getLocalFromTime(){
        return Utils.localDateTimeFromMill(fromTime);
    }

    public LocalDateTime getLocalToTime(){
        return Utils.localDateTimeFromMill(toTime);
    }
}



