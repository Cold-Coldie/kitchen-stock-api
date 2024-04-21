package com.coldie.kitchenstocks.marketlist.exception;

public class MarketListItemAlreadyExistsException extends RuntimeException {
    public MarketListItemAlreadyExistsException(String message) {
        super(message);
    }
}
