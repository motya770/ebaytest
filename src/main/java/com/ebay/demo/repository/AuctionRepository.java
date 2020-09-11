package com.ebay.demo.repository;

import com.ebay.demo.model.Auction;
import com.ebay.demo.model.EbayItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface AuctionRepository extends JpaRepository<Auction, Long> {


    @Query(" select a from Auction a where (a.fromTime <= :toTime and a.toTime >= :toTime)" +
            " or (a.fromTime <= :fromTime and a.toTime >= :fromTime ) " +
            " or (a.fromTime >= :fromTime and a.toTime <= :toTime ) and a.ebayItem.id = :#{#ebayItem.id} ")
    List<Auction> findOverlapping(@Param(value = "fromTime") Date fromTime,
                                  @Param(value = "toTime") Date toTime,
                                  @Param(value = "ebayItem") EbayItem ebayItem);
    @Modifying
    @Query("delete from Auction a where a.ebayItem.id = :itemId")
    Auction deleteByEbayItemId(@Param(value = "itemId") String itemId);

    @Modifying
    @Query("delete from Auction a where a.ebayItem.id = :fromTime")
    List<Auction> deleteByFromTime(@Param(value = "fromTime") Date fromTime);
}
