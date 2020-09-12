package com.ebay.demo.service;

import com.ebay.demo.model.Auction;
import com.ebay.demo.model.EbayItem;

import java.time.LocalDateTime;
import java.util.List;

public interface IAuctionService {

    List<Auction> getNextAuctions();

    List<Auction> removeByFromTime(LocalDateTime fromTime);

    Auction removeByEbayItemId(String itemId);

    Auction createAuction(LocalDateTime fromTime, LocalDateTime toTime, String itemId);

    List<Auction> findOverlapping(Auction auction, EbayItem ebayItem);

    void validateAuctionTimes(Auction auction);
}
