package com.coldie.kitchenstocks.item.service;

import com.coldie.kitchenstocks.exception.ItemAlreadyExistsException;
import com.coldie.kitchenstocks.exception.ItemNotFoundException;
import com.coldie.kitchenstocks.item.model.Item;
import com.coldie.kitchenstocks.item.respository.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ItemServiceImpl implements ItemService {

    @Autowired
    private ItemRepository itemRepository;

    @Override
    public List<Item> getAllItemsByUserId(Long userId) {
        return itemRepository.findByUserId(userId);
    }

    @Override
    public Item getItemByIDAndUserId(Long itemId, Long userId) {
        return itemRepository.findByIdAndUserId(itemId, userId).orElseThrow(() -> new ItemNotFoundException("Item with id: " + itemId + " not found."));
    }

    @Override
    public List<Item> getItemByNameAndUserId(String name, Long userId) {
        return itemRepository.findByNameContainingAndUserId(name, userId);
    }

    @Override
    public Item createItem(Item item) {
        Optional<Item> optionalItem = itemRepository.findByNameEqualsAndUserId(item.getName(), item.getUser().getId());

        if (optionalItem.isPresent()) {
            throw new ItemAlreadyExistsException("Item with the name: " + item.getName() + " already exists.");
        } else {
            return itemRepository.save(item);
        }
    }
}
