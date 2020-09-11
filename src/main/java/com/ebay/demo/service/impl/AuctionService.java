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

import java.time.Duration;
import java.time.LocalDateTime;
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


//     5. An auction cannot last for more than 3 hours, and for less than 15 minutes.
//     6. No auctions on Mondays.
//     7. No auctions at night (from 11:00 P.M. to 8:00 A.M).
//     8. No more than 8 hours of auctions total per day.
//     9. No more than 40 hours of auctions total per week.
    @Override
    public void validateAuctionTimes(Auction auction) {

        LocalDateTime fromTime = auction.getFromTime();
        LocalDateTime toTime = auction.getToTime();

        Duration auctionDuration = Duration.between(fromTime, toTime);
        if(auctionDuration.isNegative() || auctionDuration.isZero()){
            throw new AuctionException("Can't create auction: start time is greater than end time");
        }

        if(auctionDuration.compareTo(Duration.ofHours(3))>0){
            throw new AuctionException("Can't create auction: auction lasts more than 3 hours");
        }

        if(auctionDuration.compareTo(Duration.ofMinutes(15))<=0){
            throw new AuctionException("Can't create auction: auction lasts less than 15 minutes");
        }



        List<Auction> overlappingAuctions = findOverlapping(auction, auction.getEbayItem());

        if(overlappingAuctions.size()>0){

            //building string with auction's ids
            String overlappingIds = overlappingAuctions.stream()
                    .map(overlappingAuction->overlappingAuction.getId().toString())
                    .collect(Collectors.joining(","));

            throw new AuctionException("Can't create auction: Found overlapping auctions: { "
                    + overlappingIds + " }");
        }


    }

    @Override
    public Auction removeByEbayItemId(String itemId) {
        return auctionRepository.deleteByEbayItemId(itemId);
    }

    @Override
    public List<Auction> removeByFromTime(LocalDateTime fromTime) {
        return auctionRepository.deleteByFromTime(fromTime);
    }

    @Override
    public List<Auction> getNextAuctions() {
        return auctionRepository.findByFromTimeGreaterThanOrderByFromTimeAsc(LocalDateTime.now(),
                PageRequest.of(0, 100));
    }

    @Override
    public Auction createAuction(LocalDateTime fromTime, LocalDateTime toTime, String itemId) {

        Auction auction = new Auction();
        auction.setFromTime(fromTime);
        auction.setToTime(toTime);

        EbayItem ebayItem = ebayItemRepository.findById(itemId).get();
        if(ebayItem==null){
            throw new AuctionException("Can't create auction: Ebay item " + itemId + " not found");
        }
        auction.setEbayItem(ebayItem);

        validateAuctionTimes(auction);

        return auctionRepository.save(auction);
    }


}
