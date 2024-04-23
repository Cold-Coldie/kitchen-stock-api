package com.coldie.kitchenstocks.item.service;

import com.coldie.kitchenstocks.item.model.Item;
import com.coldie.kitchenstocks.item.request.ItemRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface ItemService {

    Page<Item> getAllItems(Pageable pageable);

    Page<Item> getItemsByName(String name, Pageable pageable);

    Page<Item> getItemById(Long id, Pageable pageable);

    Item createItem(ItemRequest itemRequest);

    Item updateItem(Item item);

    String deleteItem(Long id);
}
