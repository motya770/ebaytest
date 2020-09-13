package com.ebay.demo.repository;

import com.ebay.demo.model.Auction;
import com.ebay.demo.model.EbayItem;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AuctionRepository extends JpaRepository<Auction, Long> {

    @Query(" select a from Auction a where (a.fromTime <= :toTime and a.toTime >= :toTime)" +
            " or (a.fromTime <= :fromTime and a.toTime >= :fromTime ) " +
            " or (a.fromTime >= :fromTime and a.toTime <= :toTime )" +
            " and a.ebayItem.id = :#{#ebayItem.id} ")
    List<Auction> findOverlapping(@Param(value = "fromTime") Long fromTime,
                                  @Param(value = "toTime") Long toTime,
                                  @Param(value = "ebayItem") EbayItem ebayItem);


    @Query("select a from Auction a where a.ebayItem.id = :itemId")
    List<Auction> selectByEbayItemId(@Param(value = "itemId") String itemId);

    @Query("delete a from Auction a where a.ebayItem.id = :fromTime")
    List<Auction> selectByFromTime(@Param(value = "fromTime") LocalDateTime fromTime);

    //used native query because we are using
    @Query(value =
            " select SUM(a.toTime - a.fromTime) from Auction a " +
                    " where a.fromTime >= :fromTime and a.toTime <= :toTime ")
    Long getTotalTimeFrameSum(@Param(value = "fromTime") Long fromTime,
                              @Param(value = "toTime") Long toTime);

    List<Auction> findByFromTimeGreaterThanOrderByFromTimeAsc(LocalDateTime fromTime, Pageable pageable);

    List<Auction> findByFromTimeAndToTimeAndEbayItem_Id(Long fromTime, Long toTime, String ebayItem_Id);

}