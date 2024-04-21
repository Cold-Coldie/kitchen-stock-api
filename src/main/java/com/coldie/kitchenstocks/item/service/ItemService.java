package com.coldie.kitchenstocks.item.service;

import com.coldie.kitchenstocks.item.model.Item;
import com.coldie.kitchenstocks.item.request.ItemRequest;

import java.util.List;
import java.util.Optional;

public interface ItemService {

    Item createItem(ItemRequest itemRequest);
}
