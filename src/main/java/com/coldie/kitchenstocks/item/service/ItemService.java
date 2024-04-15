package com.coldie.kitchenstocks.item.service;

import com.coldie.kitchenstocks.item.model.Item;

import java.util.List;
import java.util.Optional;

public interface ItemService {
    List<Item> getAllItemsByUserId(Long userId);

    Item getItemByIDAndUserId(Long itemId, Long userId);

    List<Item> getItemByNameAndUserId(String name, Long userId);

    Item createItem(Item item);
}
