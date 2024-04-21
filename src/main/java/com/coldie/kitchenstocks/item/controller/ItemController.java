package com.coldie.kitchenstocks.item.controller;

import com.coldie.kitchenstocks.exception.UnexpectedErrorException;
import com.coldie.kitchenstocks.item.model.Item;
import com.coldie.kitchenstocks.item.request.ItemRequest;
import com.coldie.kitchenstocks.item.respository.ItemRepository;
import com.coldie.kitchenstocks.item.service.ItemService;
import com.coldie.kitchenstocks.measuringUnit.model.MeasuringUnit;
import com.coldie.kitchenstocks.measuringUnit.service.MeasuringUnitService;
import com.coldie.kitchenstocks.user.model.User;
import com.coldie.kitchenstocks.user.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("api/v1/item")
public class ItemController {

    @Autowired
    private ItemService itemService;

    @Autowired
    private UserService userService;

    @Autowired
    private MeasuringUnitService measuringUnitService;

    @PostMapping("/create")
    public ResponseEntity<Item> createItem(@Valid @RequestBody ItemRequest itemRequest) {
       return new ResponseEntity<Item>(itemService.createItem(itemRequest), HttpStatus.CREATED);
    }


}
