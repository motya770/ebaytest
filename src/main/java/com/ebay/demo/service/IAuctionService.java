package com.ebay.demo.service;

import com.ebay.demo.model.Auction;
import com.ebay.demo.model.EbayItem;

import java.util.Date;
import java.util.List;

public interface IAuctionService {

    List<Auction> getNextAuctions();

    List<Auction> removeByFromTime(Date fromTime);

    Auction removeByEbayItemId(String itemId);

    Auction createAuction(Date fromTime, Date toTime, String itemId);

    List<Auction> findOverlapping(Auction auction, EbayItem ebayItem);
}
