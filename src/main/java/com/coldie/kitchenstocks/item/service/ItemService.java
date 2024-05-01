package com.coldie.kitchenstocks.item.service;

import com.coldie.kitchenstocks.item.model.Item;
import com.coldie.kitchenstocks.item.request.ItemRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface ItemService {

    Page<Item> getAllItems(Pageable pageable);

    Page<Item> getItemsByName(String name, Pageable pageable);

    Item getItemById(Long id);

    Item createItem(ItemRequest itemRequest);

    Item updateItem(ItemRequest itemRequest);

    String deleteItemById(Long id);
}
