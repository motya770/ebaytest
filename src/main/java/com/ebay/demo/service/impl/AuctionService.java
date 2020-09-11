package com.ebay.demo.service.impl;

import com.ebay.demo.exception.AuctionException;
import com.ebay.demo.model.Auction;
import com.ebay.demo.model.EbayItem;
import com.ebay.demo.repository.AuctionRepository;
import com.ebay.demo.repository.EbayItemRepository;
import com.ebay.demo.service.IAuctionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Transactional
@Service
public class AuctionService implements IAuctionService {

    @Autowired
    private AuctionRepository auctionRepository;

    @Autowired
    private EbayItemRepository ebayItemRepository;

    @Override
    public List<Auction> findOverlapping(Auction auction, EbayItem ebayItem) {
        return auctionRepository.findOverlapping(auction.getFromTime(), auction.getToTime(), ebayItem);
    }

    @Override
    public Auction removeByEbayItemId(String itemId) {
        return auctionRepository.deleteByEbayItemId(itemId);
    }

    @Override
    public List<Auction> removeByFromTime(Date fromTime) {
        return auctionRepository.deleteByFromTime(fromTime);
    }

    @Override
    public List<Auction> getNextAuctions() {
        return auctionRepository.findByFromTimeGreaterThanOrderByFromTimeAsc(Calendar.getInstance().getTime(),
                PageRequest.of(0, 100));
    }

    @Override
    public Auction createAuction(Date fromTime, Date toTime, String itemId) {

        if(fromTime.compareTo(toTime) >= 0){
            throw new AuctionException("Can't create auction: start time is greater than end time");
        }

        EbayItem ebayItem = ebayItemRepository.findById(itemId).get();
        if(ebayItem==null){
            throw new AuctionException("Can't create auction: Ebay item " + itemId + " not found");
        }

        Auction auction = new Auction();
        auction.setFromTime(fromTime);
        auction.setToTime(toTime);
        auction.setEbayItem(ebayItem);

        List<Auction> overlappingAuctions = findOverlapping(auction, ebayItem);

        if(overlappingAuctions.size()>0){

            //building string with auction's ids
            String overlappingIds = overlappingAuctions.stream()
                    .map(overlappingAuction->overlappingAuction.getId().toString())
                    .collect(Collectors.joining(","));

            throw new AuctionException("Can't create auction: Found overlapping auctions: { "
                    + overlappingIds + " }");
        }

        return auctionRepository.save(auction);
    }


}
