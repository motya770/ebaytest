package com.ebay.demo.service.impl;

import com.ebay.demo.model.EbayItem;
import com.ebay.demo.repository.EbayItemRepository;
import com.ebay.demo.service.IEbayItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EbayItemService implements IEbayItemService  {

    @Autowired
    private EbayItemRepository ebayItemRepository;

    @Override
    public EbayItem createEbayItem(String uuid, String name) {
        EbayItem ebayItem = new EbayItem();
        ebayItem.setId(uuid);
        ebayItem.setItemName(name);
        ebayItem =  ebayItemRepository.save(ebayItem);
        return ebayItem;
    }
}
