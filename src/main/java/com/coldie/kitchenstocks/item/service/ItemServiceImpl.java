package com.coldie.kitchenstocks.item.service;

import com.coldie.kitchenstocks.config.SecurityUtils;
import com.coldie.kitchenstocks.config.exception.NotAuthenticatedException;
import com.coldie.kitchenstocks.exception.InvalidRequest;
import com.coldie.kitchenstocks.exception.UnexpectedErrorException;
import com.coldie.kitchenstocks.item.exception.ItemAlreadyExistsException;
import com.coldie.kitchenstocks.item.exception.ItemNotFoundException;
import com.coldie.kitchenstocks.item.model.Item;
import com.coldie.kitchenstocks.item.request.ItemRequest;
import com.coldie.kitchenstocks.item.respository.ItemRepository;
import com.coldie.kitchenstocks.measuringUnit.exception.MeasuringUnitNotFoundException;
import com.coldie.kitchenstocks.measuringUnit.model.MeasuringUnit;
import com.coldie.kitchenstocks.measuringUnit.repository.MeasuringUnitRepository;
import com.coldie.kitchenstocks.user.exception.UserNotFoundException;
import com.coldie.kitchenstocks.user.model.User;
import com.coldie.kitchenstocks.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class ItemServiceImpl implements ItemService {

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MeasuringUnitRepository measuringUnitRepository;

    @Override
    public Page<Item> getAllItems(Pageable pageable) {
        try {
            UserDetails userDetails = SecurityUtils.getCurrentUserDetails();
            if (userDetails == null) throw new NotAuthenticatedException("Please, re-authenticate.");

            return itemRepository.findAllByUser_EmailEquals(userDetails.getUsername(), pageable);
        } catch (UnexpectedErrorException exception) {
            throw new UnexpectedErrorException("An unexpected error occurred.");
        }
    }

    @Override
    public Page<Item> getItemsByName(String name, Pageable pageable) {
        try {
            UserDetails userDetails = SecurityUtils.getCurrentUserDetails();
            if (userDetails == null) throw new NotAuthenticatedException("Please, re-authenticate.");

            return itemRepository.findAllByUser_EmailEqualsAndNameContaining(userDetails.getUsername(), name, pageable);
        } catch (UnexpectedErrorException exception) {
            throw new UnexpectedErrorException("An unexpected error occurred.");
        }
    }

    @Override
    public Item getItemById(Long id) {
        try {
            UserDetails userDetails = SecurityUtils.getCurrentUserDetails();
            if (userDetails == null) throw new NotAuthenticatedException("Please, re-authenticate.");

            return itemRepository.findByUser_EmailEqualsAndIdEquals(userDetails.getUsername(), id)
                    .orElseThrow(() -> new ItemNotFoundException("Item with id: " + id + " not found."));
        } catch (UnexpectedErrorException exception) {
            throw new UnexpectedErrorException("An unexpected error occurred.");
        }
    }

    @Override
    public Item createItem(ItemRequest itemRequest) {
        try {
            UserDetails userDetails = SecurityUtils.getCurrentUserDetails();
            if (userDetails == null) throw new NotAuthenticatedException("Please, re-authenticate.");

            itemRepository.findByUser_EmailEqualsAndNameEquals(userDetails.getUsername(), itemRequest.getName())
                    .ifPresent(item -> {
                        throw new ItemAlreadyExistsException("Item with the name: " + itemRequest.getName() + " already exists.");
                    });

            User user = userRepository.findByEmailEquals(userDetails.getUsername()).orElseThrow(() -> new UserNotFoundException("User with this email does not exist."));

            MeasuringUnit measuringUnit = measuringUnitRepository.findByUser_EmailEqualsAndIdEquals(userDetails.getUsername(), itemRequest.getMeasuringUnitId())
                    .orElseThrow(() -> new MeasuringUnitNotFoundException("Measuring unit with this id: " + itemRequest.getMeasuringUnitId() + " does not exist."));

            return itemRepository.save(getItemToSave(user, measuringUnit, itemRequest));
        } catch (UnexpectedErrorException exception) {
            throw new UnexpectedErrorException("An unexpected error occurred.");
        }
    }

    @Override
    public Item updateItem(ItemRequest itemRequest) {
        UserDetails userDetails = SecurityUtils.getCurrentUserDetails();
        if (userDetails == null) throw new NotAuthenticatedException("Please, re-authenticate.");

        if (itemRequest.getId() == null)
            throw new InvalidRequest("Please provide the \"id\" of the item in your request.");

        Item savedItem = itemRepository.findByUser_EmailEqualsAndIdEquals(userDetails.getUsername(), itemRequest.getId())
                .orElseThrow(() -> new ItemNotFoundException("Item with id: " + itemRequest.getId() + " not found."));

        if (!Objects.equals(savedItem.getMeasuringUnit().getId(), itemRequest.getMeasuringUnitId())) {
            MeasuringUnit measuringUnit = measuringUnitRepository.findByUser_EmailEqualsAndIdEquals(userDetails.getUsername(), itemRequest.getMeasuringUnitId())
                    .orElseThrow(() -> new MeasuringUnitNotFoundException("Measuring unit with id: " + itemRequest.getMeasuringUnitId() + " not found."));

            savedItem.setMeasuringUnit(measuringUnit);
        }

        return itemRepository.save(getItemToUpdate(savedItem, itemRequest));
    }

    @Override
    public String deleteItemById(Long id) {
        try {
            UserDetails userDetails = SecurityUtils.getCurrentUserDetails();
            if (userDetails == null) throw new NotAuthenticatedException("Please, re-authenticate.");

            itemRepository.findByUser_EmailEqualsAndIdEquals(userDetails.getUsername(), id)
                    .ifPresentOrElse(item -> itemRepository.deleteById(item.getId()),
                            () -> {
                                throw new ItemNotFoundException("Item with the id: " + id + " not found.");
                            }
                    );

            return "Deleted item with id: " + id;
        } catch (UnexpectedErrorException exception) {
            throw new UnexpectedErrorException("An unexpected error occurred.");
        }
    }

    private static Item getItemToSave(User user, MeasuringUnit measuringUnit, ItemRequest itemRequest) {
        return new Item(
                itemRequest.getName(),
                itemRequest.getAvailableQuantity(),
                1,
                itemRequest.getLowLimit(),
                itemRequest.getPrice(),
                itemRequest.getCurrencyName(),
                itemRequest.getCurrencySymbol(),
                itemRequest.getAvailableQuantity() <= itemRequest.getLowLimit(),
                user,
                measuringUnit
        );
    }

    private static Item getItemToUpdate(Item savedItem, ItemRequest itemRequest) {
        if (Objects.nonNull(itemRequest.getName())) {
            savedItem.setName(itemRequest.getName());
        }
        if (Objects.nonNull(itemRequest.getAvailableQuantity())) {
            savedItem.setAvailableQuantity(itemRequest.getAvailableQuantity());
        }
        if (Objects.nonNull(itemRequest.getRestockQuantity())) {
            savedItem.setRestockQuantity(itemRequest.getRestockQuantity());
        }
        if (Objects.nonNull(itemRequest.getLowLimit())) {
            savedItem.setLowLimit(itemRequest.getLowLimit());
        }
        if (Objects.nonNull(itemRequest.getPrice())) {
            savedItem.setPrice(itemRequest.getPrice());
        }
        if (Objects.nonNull(itemRequest.getCurrencyName())) {
            savedItem.setCurrencyName(itemRequest.getCurrencyName());
        }
        if (Objects.nonNull(itemRequest.getCurrencySymbol())) {
            savedItem.setCurrencySymbol(itemRequest.getCurrencySymbol());
        }
        if (Objects.nonNull(itemRequest.getNeedRestock())) {
            savedItem.setNeedRestock(itemRequest.getNeedRestock());
        } else {
            savedItem.setNeedRestock(savedItem.getAvailableQuantity() <= savedItem.getLowLimit());
        }

        return savedItem;
    }
}
