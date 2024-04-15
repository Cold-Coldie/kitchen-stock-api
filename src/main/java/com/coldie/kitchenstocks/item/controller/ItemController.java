package com.coldie.kitchenstocks.item.controller;

import com.coldie.kitchenstocks.exception.UnexpectedErrorException;
import com.coldie.kitchenstocks.item.model.Item;
import com.coldie.kitchenstocks.item.request.ItemRequest;
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

    @GetMapping("")
    public ResponseEntity<List<Item>> getItems(@RequestParam(name = "id", required = false) Long id,
                                               @RequestParam(name = "name", required = false) String name
    ) {
        try {
            Long userId = 4L;

            List<Item> items = new ArrayList<>();

            if (id == null && name == null) {
                items = itemService.getAllItemsByUserId(userId);
            } else if (id != null && name == null) {
                Item itemById = itemService.getItemByIDAndUserId(id, userId);
                items.add(itemById);
            } else if (name != null && id == null) {
                items = itemService.getItemByNameAndUserId(name, userId);
            }

            return new ResponseEntity<List<Item>>(items, HttpStatus.OK);
        } catch (UnexpectedErrorException exception) {
            throw new UnexpectedErrorException("An unexpected error occurred.");
        }
    }

    @PostMapping("/create")
    public ResponseEntity<Item> createItem(@Valid @RequestBody ItemRequest itemRequest) {
        try {
            Long userId = 4L;

            User user = userService.getUserById(userId);
            MeasuringUnit measuringUnit = measuringUnitService.getMeasuringUnitByIdAndUserId(itemRequest.getMeasuringUnitId(), userId);

            Item item = getItemToSave(user, measuringUnit, itemRequest);

            Item createdItem = itemService.createItem(item);

            return new ResponseEntity<Item>(createdItem, HttpStatus.CREATED);
        } catch (UnexpectedErrorException exception) {
            throw new UnexpectedErrorException("An unexpected error occurred.");
        }
    }

    public static Item getItemToSave(User user, MeasuringUnit measuringUnit, ItemRequest itemRequest) {
        Item item = new Item();

        item.setName(itemRequest.getName());
        item.setQuantity(itemRequest.getQuantity());
        item.setLowLimit(itemRequest.getLowLimit());
        item.setPrice(itemRequest.getPrice());
        item.setCurrencyName(itemRequest.getCurrencyName());
        item.setCurrencySymbol(itemRequest.getCurrencySymbol());

        item.setUser(user);
        item.setMeasuringUnit(measuringUnit);
        item.setNeedRestock(item.getQuantity() <= item.getLowLimit());

        return item;
    }
}
