package com.coldie.kitchenstocks.item.controller;

import com.coldie.kitchenstocks.item.model.Item;
import com.coldie.kitchenstocks.item.request.ItemRequest;
import com.coldie.kitchenstocks.item.service.ItemService;
import com.coldie.kitchenstocks.measuringUnit.service.MeasuringUnitService;
import com.coldie.kitchenstocks.user.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<Page<Item>> getItems(
            @RequestParam(value = "id", required = false) Long id,
            @RequestParam(value = "name", required = false) String name,
            @PageableDefault(page = 0, size = 10, sort = {"id"}, direction = Sort.Direction.ASC) Pageable pageable) {

        if (id == null && name == null) {
            return new ResponseEntity<Page<Item>>(itemService.getAllItems(pageable), HttpStatus.OK);
        } else if (id != null && name == null) {
            return new ResponseEntity<Page<Item>>(itemService.getItemById(id, pageable), HttpStatus.OK);
        } else {
            return new ResponseEntity<Page<Item>>(itemService.getItemsByName(name, pageable), HttpStatus.OK);
        }
    }

    @PostMapping("")
    public ResponseEntity<Item> createItem(@Valid @RequestBody ItemRequest itemRequest) {
        return new ResponseEntity<Item>(itemService.createItem(itemRequest), HttpStatus.CREATED);
    }

    @PutMapping("")
    public ResponseEntity<Item> updateItem(@RequestBody Item item) {
        return new ResponseEntity<Item>(itemService.updateItem(item), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> createItem(@PathVariable("id") Long id) {
        return new ResponseEntity<String>(itemService.deleteItem(id), HttpStatus.OK);
    }
}
