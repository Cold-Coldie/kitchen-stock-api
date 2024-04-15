package com.coldie.kitchenstocks.exception;

import java.util.List;

public class ErrorResponse {
    private String status;
    private String message;
    private List<String> details;

    public ErrorResponse(String status, String message, List<String> details) {
        this.status = status;
        this.message = message;
        this.details = details;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<String> getDetails() {
        return details;
    }

    public void setDetails(List<String> details) {
        this.details = details;
    }
}
