package com.ebay.demo.repository;

import com.ebay.demo.model.EbayItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EbayItemRepository extends JpaRepository<EbayItem, String> {
}
