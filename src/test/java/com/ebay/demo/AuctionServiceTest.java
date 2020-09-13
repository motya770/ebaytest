package com.ebay.demo;

import com.ebay.demo.service.IAuctionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class AuctionServiceTest {

    @Autowired
    private IAuctionService auctionService;

    @Test
    public void test(){

    }
}
