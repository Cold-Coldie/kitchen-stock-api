package com.coldie.kitchenstocks.item.request;

import java.math.BigDecimal;

public class ItemRequest {

    private String name;

    private Integer quantity;

    private Integer lowLimit;

    private BigDecimal price;

    private String currencyName;

    private String currencySymbol;

    private Long measuringUnitId;

    public ItemRequest(
            String name,
            Integer quantity,
            Integer lowLimit,
            BigDecimal price,
            String currencyName,
            String currencySymbol,
            Long measuringUnitId
    ) {
        this.name = name;
        this.quantity = quantity;
        this.lowLimit = lowLimit;
        this.price = price;
        this.currencyName = currencyName;
        this.currencySymbol = currencySymbol;
        this.measuringUnitId = measuringUnitId;
    }

    public ItemRequest() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Integer getLowLimit() {
        return lowLimit;
    }

    public void setLowLimit(Integer lowLimit) {
        this.lowLimit = lowLimit;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getCurrencyName() {
        return currencyName;
    }

    public void setCurrencyName(String currencyName) {
        this.currencyName = currencyName;
    }

    public String getCurrencySymbol() {
        return currencySymbol;
    }

    public void setCurrencySymbol(String currencySymbol) {
        this.currencySymbol = currencySymbol;
    }

    public Long getMeasuringUnitId() {
        return measuringUnitId;
    }

    public void setMeasuringUnitId(Long measuringUnitId) {
        this.measuringUnitId = measuringUnitId;
    }
}
