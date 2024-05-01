package com.coldie.kitchenstocks.exception;

public class InvalidRequest extends RuntimeException{
    public InvalidRequest(String message) {
        super(message);
    }
}
