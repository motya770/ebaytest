package com.ebay.demo.controller;

import com.ebay.demo.model.Auction;
import com.ebay.demo.service.IAuctionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping(value = "/auction")
public class AuctionController {

    @Autowired
    private IAuctionService auctionService;

    //    1. Set Auction - with parameters fromTime, toTime, itemId.
    //    2. Remove Auction - with parameter fromTime
    //    3. Remove Auction - with parameter itemId
    //    4. Get Next Auction - with no parameters

    @PostMapping(value = "/set-auction")
    public Auction setAuction(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)  LocalDateTime fromTime,
                              @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime toTime,
                              @RequestParam String itemId) {
        return auctionService.createAuction(fromTime, toTime, itemId);
    }

    //TODO fix and think
    @PostMapping(value = "/remove")
    public Auction removeAuction(@RequestParam LocalDateTime fromTime,
                                 @RequestParam String itemId) {
        return auctionService.removeByEbayItemId(itemId);
    }

    @GetMapping(value = "/get-next")
    public List<Auction> getNexAuction() {
        return auctionService.getNextAuctions();
    }



}
