package com.ebay.demo.controller;

import com.ebay.demo.model.Auction;
import com.ebay.demo.service.IAuctionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping(value = "/auction")
public class AuctionController {

    @Autowired
    private IAuctionService auctionService;

    @PostMapping(value = "/set-auction")
    public Auction setAuction(Date fromTime, Date toTime, String itemId) {
        return null;
    }

    @PostMapping(value = "/remove")
    public Auction removeAuction(Date fromTime, String itemId) {
        return null;
    }

    @GetMapping(value = "/get-next")
    public List<Auction> getNexAuction() {
        return null;
    }




    //    1. Set Auction - with parameters fromTime, toTime, itemId.
    //    2. Remove Auction - with parameter fromTime
    //    3. Remove Auction - with parameter itemId
    //    4. Get Next Auction - with no parameters
}
