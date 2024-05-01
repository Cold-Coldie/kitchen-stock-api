package com.coldie.kitchenstocks.item.request;

import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public class ItemRequest {

    private Long id;

    @NotNull(message = "name cannot be null")
    private String name;

    @NotNull(message = "availableQuantity cannot be null")
    private Integer availableQuantity;

    private Integer restockQuantity;

    @NotNull(message = "lowLimit cannot be null")
    private Integer lowLimit;

    @NotNull(message = "price cannot be null")
    private BigDecimal price;

    @NotNull(message = "currencyName cannot be null")
    private String currencyName;

    @NotNull(message = "currencySymbol cannot be null")
    private String currencySymbol;

    private Boolean needRestock;

    private Long measuringUnitId;

    public ItemRequest(
            String name,
            Integer availableQuantity,
            Integer restockQuantity,
            Integer lowLimit,
            BigDecimal price,
            String currencyName,
            String currencySymbol,
            Boolean needRestock,
            Long measuringUnitId
    ) {
        this.name = name;
        this.availableQuantity = availableQuantity;
        this.restockQuantity = restockQuantity;
        this.lowLimit = lowLimit;
        this.price = price;
        this.currencyName = currencyName;
        this.currencySymbol = currencySymbol;
        this.needRestock = needRestock;
        this.measuringUnitId = measuringUnitId;
    }

    public ItemRequest() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAvailableQuantity() {
        return availableQuantity;
    }

    public void setAvailableQuantity(Integer availableQuantity) {
        this.availableQuantity = availableQuantity;
    }

    public Integer getRestockQuantity() {
        return restockQuantity;
    }

    public void setRestockQuantity(Integer restockQuantity) {
        this.restockQuantity = restockQuantity;
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

    public Boolean getNeedRestock() {
        return needRestock;
    }

    public void setNeedRestock(Boolean needRestock) {
        this.needRestock = needRestock;
    }

    public Long getMeasuringUnitId() {
        return measuringUnitId;
    }

    public void setMeasuringUnitId(Long measuringUnitId) {
        this.measuringUnitId = measuringUnitId;
    }
}
