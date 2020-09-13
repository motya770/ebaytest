package com.ebay.demo.exception;

public class AlreadyScheduledException extends AuctionException {

    public AlreadyScheduledException(String message){
        super(message);
    }
}