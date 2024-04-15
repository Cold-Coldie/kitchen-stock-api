package com.coldie.kitchenstocks.measuringUnit.model;

import com.coldie.kitchenstocks.item.model.Item;
import com.coldie.kitchenstocks.user.model.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.util.List;

@Entity
@Table(name = "MEASURING_UNITS")
public class MeasuringUnit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    @NotNull(message = "name cannot be null")
    private String name;

    @OneToMany(mappedBy = "measuringUnit")
    @JsonIgnore
    private List<Item> itemList;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;

    public MeasuringUnit(Long id, String name, List<Item> itemList, User user) {
        this.id = id;
        this.name = name;
        this.itemList = itemList;
        this.user = user;
    }

    public MeasuringUnit() {
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

    public List<Item> getItemList() {
        return itemList;
    }

    public void setItemList(List<Item> itemList) {
        this.itemList = itemList;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "MeasuringUnit{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", itemList=" + itemList +
                ", user=" + user +
                '}';
    }
}
