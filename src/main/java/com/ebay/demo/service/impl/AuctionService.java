package com.ebay.demo.service.impl;

import com.ebay.demo.exception.AuctionException;
import com.ebay.demo.exception.DayFullAuctionException;
import com.ebay.demo.exception.WeekFullAuctionException;
import com.ebay.demo.model.Auction;
import com.ebay.demo.model.EbayItem;
import com.ebay.demo.repository.AuctionRepository;
import com.ebay.demo.repository.EbayItemRepository;
import com.ebay.demo.service.IAuctionService;
import com.ebay.demo.service.IEbayItemService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.swing.text.html.Option;
import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Transactional
@Service
public class AuctionService implements IAuctionService {

    @Autowired
    private AuctionRepository auctionRepository;

    @Autowired
    private EbayItemRepository ebayItemRepository;

    @Autowired
    private IEbayItemService ebayItemService;

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
            throw new AuctionException("Can't create auction: start time is greater than end time. ItemId " + auction.getEbayItem().getId());
        }

        if(auctionDuration.compareTo(Duration.ofHours(3))>0){
            throw new AuctionException("Can't create auction: auction lasts more than 3 hours. ItemId " + auction.getEbayItem().getId());
        }

        if(auctionDuration.compareTo(Duration.ofMinutes(15))<=0){
            throw new AuctionException("Can't create auction: auction lasts less than 15 minutes. ItemId " + auction.getEbayItem().getId());
        }

        if(fromTime.getDayOfWeek() == DayOfWeek.MONDAY || toTime.getDayOfWeek() == DayOfWeek.MONDAY){
            throw new DayFullAuctionException("Can't create auction: No auctions on Mondays. ItemId " + auction.getEbayItem().getId());
        }

        if(fromTime.getHour() >= 23 || toTime.getHour()<=8){
            throw new AuctionException("Can't create auction: No auctions at night (from 11:00 P.M. to 8:00 A.M). ItemId " + auction.getEbayItem().getId());
        }

        checkLimitPerDay(auction);
        checkLimitPerWeek(auction);
        checkOverlappingTimes(auction);
    }

    private void checkOverlappingTimes(Auction auction) {
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

    private void checkLimitPerWeek(Auction auction) {
        LocalDateTime today = LocalDateTime.now();
        LocalDateTime firstDayOfTheWeek = today.with(DayOfWeek.MONDAY);
        LocalDateTime lastDayOfTheWeek = today.with(DayOfWeek.SUNDAY);
        LocalDateTime lastDayOfTheWeekAndLastHour  = LocalDateTime.of(lastDayOfTheWeek.toLocalDate(), LocalTime.MAX);

        Long millTotalForWeekSum = auctionRepository.getTotalTimeFrameSum(firstDayOfTheWeek, lastDayOfTheWeekAndLastHour);
        if(millTotalForWeekSum==null){
            return;
        }

        Duration maxDuration =  Duration.ofHours(40);
        Duration totalSumWeekDuration = Duration.of(millTotalForWeekSum, ChronoUnit.MILLIS);

        if(maxDuration.compareTo(totalSumWeekDuration) <= 0){
            throw new WeekFullAuctionException("Can't create auction: No more than 40 hours of auctions total per week. ItemId " + auction.getEbayItem().getId());
        }
    }

    private void checkLimitPerDay(Auction auction) {
        LocalDateTime today = LocalDateTime.now();
        LocalDateTime startOfToday = LocalDateTime.of(today.toLocalDate(), LocalTime.MIDNIGHT);
        LocalDateTime endOfToday  = LocalDateTime.of(today.toLocalDate(), LocalTime.MAX);

        Long millTotalSum = auctionRepository.getTotalTimeFrameSum(startOfToday, endOfToday);
        if(millTotalSum==null){
            return;
        }
        Duration hoursDuration =  Duration.ofHours(8);
        Duration totalSumTodayDuration = Duration.of(millTotalSum, ChronoUnit.MILLIS);

        if(hoursDuration.compareTo(totalSumTodayDuration) <= 0){
            throw new DayFullAuctionException("Can't create auction: No more than 8 hours of auctions total per day. ItemId " + auction.getEbayItem().getId());
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

        EbayItem ebayItem = null;
        Optional<EbayItem> ebayItemOptional = ebayItemRepository.findById(itemId);
        if(ebayItemOptional.isEmpty()){
            ebayItem = ebayItemService.createEbayItem(itemId);
        }else {
            ebayItem = ebayItemOptional.get();
        }

        auction.setEbayItem(ebayItem);

        validateAuctionTimes(auction);

        return auctionRepository.save(auction);
    }


}
