package com.coldie.kitchenstocks.item.model;

import com.coldie.kitchenstocks.measuringUnit.model.MeasuringUnit;
import com.coldie.kitchenstocks.user.model.User;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "ITEMS")
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    @NotNull(message = "name cannot be null")
    private String name;

    @Column(name = "quantity")
    @NotNull(message = "quantity cannot be null")
    private Integer quantity;

    @Column(name = "low_limit")
    @NotNull(message = "lowLimit cannot be null")
    private Integer lowLimit;

    @Column(name = "price")
    @NotNull(message = "price cannot be null")
    private BigDecimal price;

    @Column(name = "currency_name")
    @NotNull(message = "currencyName cannot be null")
    private String currencyName;

    @Column(name = "currency_symbol")
    @NotNull(message = "currencySymbol cannot be null")
    private String currencySymbol;

    @Column(name = "need_restock")
    private Boolean needRestock;

    @Column(name = "created_at", nullable = false, updatable = false)
    @CreationTimestamp
    private Date createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Date updatedAt;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "measuring_unit_id")
    private MeasuringUnit measuringUnit;

    public Item(
            Long id,
            String name,
            Integer quantity,
            Integer lowLimit,
            BigDecimal price,
            String currencyName,
            String currencySymbol,
            Boolean needRestock,
            Date createdAt,
            Date updatedAt,
            User user,
            MeasuringUnit measuringUnit
    ) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.lowLimit = lowLimit;
        this.price = price;
        this.currencyName = currencyName;
        this.currencySymbol = currencySymbol;
        this.needRestock = needRestock;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.user = user;
        this.measuringUnit = measuringUnit;
    }

    public Item(
            String name,
            Integer quantity,
            Integer lowLimit,
            BigDecimal price,
            String currencyName,
            String currencySymbol,
            Boolean needRestock,
            User user,
            MeasuringUnit measuringUnit
    ) {
        this.name = name;
        this.quantity = quantity;
        this.lowLimit = lowLimit;
        this.price = price;
        this.currencyName = currencyName;
        this.currencySymbol = currencySymbol;
        this.needRestock = needRestock;
        this.user = user;
        this.measuringUnit = measuringUnit;
    }

    public Item() {
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

    public Boolean getNeedRestock() {
        return needRestock;
    }

    public void setNeedRestock(Boolean needRestock) {
        this.needRestock = needRestock;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public MeasuringUnit getMeasuringUnit() {
        return measuringUnit;
    }

    public void setMeasuringUnit(MeasuringUnit measuringUnit) {
        this.measuringUnit = measuringUnit;
    }

    @Override
    public String toString() {
        return "Item{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", quantity=" + quantity +
                ", lowLimit=" + lowLimit +
                ", price=" + price +
                ", currencyName='" + currencyName + '\'' +
                ", currencySymbol='" + currencySymbol + '\'' +
                ", needRestock=" + needRestock +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", user=" + user +
                ", measuringUnit=" + measuringUnit +
                '}';
    }
}
