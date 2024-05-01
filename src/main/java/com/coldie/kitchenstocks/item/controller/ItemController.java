package com.coldie.kitchenstocks.item.controller;

import com.coldie.kitchenstocks.item.model.Item;
import com.coldie.kitchenstocks.item.request.ItemRequest;
import com.coldie.kitchenstocks.item.service.ItemService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/item")
public class ItemController {

    @Autowired
    private ItemService itemService;

    @GetMapping("")
    public ResponseEntity<Page<Item>> getItems(
            @RequestParam(name = "name", required = false) String name,
            @PageableDefault(page = 0, size = 10, sort = {"id"}, direction = Sort.Direction.DESC) Pageable pageable
    ) {
        if (name != null) {
            return new ResponseEntity<Page<Item>>(itemService.getItemsByName(name, pageable), HttpStatus.OK);
        } else {
            return new ResponseEntity<Page<Item>>(itemService.getAllItems(pageable), HttpStatus.OK);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Item> getItemById(@PathVariable(value = "id") Long id) {
        return new ResponseEntity<Item>(itemService.getItemById(id), HttpStatus.OK);
    }

    @PostMapping("")
    public ResponseEntity<Item> createItem(@Valid @RequestBody ItemRequest itemRequest) {
        return new ResponseEntity<Item>(itemService.createItem(itemRequest), HttpStatus.CREATED);
    }

    @PutMapping("")
    public ResponseEntity<Item> updateItem(@RequestBody ItemRequest itemRequest) {
        return new ResponseEntity<Item>(itemService.updateItem(itemRequest), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteItemById(@PathVariable("id") Long id) {
        return new ResponseEntity<String>(itemService.deleteItemById(id), HttpStatus.OK);
    }
}
