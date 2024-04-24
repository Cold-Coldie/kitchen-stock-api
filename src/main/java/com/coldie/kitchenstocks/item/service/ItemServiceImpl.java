package com.coldie.kitchenstocks.item.service;

import com.coldie.kitchenstocks.config.SecurityUtils;
import com.coldie.kitchenstocks.config.exception.NotAuthenticatedException;
import com.coldie.kitchenstocks.exception.UnexpectedErrorException;
import com.coldie.kitchenstocks.item.exception.ItemAlreadyExistsException;
import com.coldie.kitchenstocks.item.exception.ItemNotFoundException;
import com.coldie.kitchenstocks.item.model.Item;
import com.coldie.kitchenstocks.item.request.ItemRequest;
import com.coldie.kitchenstocks.item.respository.ItemRepository;
import com.coldie.kitchenstocks.marketlist.model.MarketList;
import com.coldie.kitchenstocks.marketlist.repository.MarketListRepository;
import com.coldie.kitchenstocks.marketlist.service.MarketListService;
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
import java.util.Optional;

@Service
public class ItemServiceImpl implements ItemService {

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MeasuringUnitRepository measuringUnitRepository;

    @Autowired
    private MarketListService marketListService;

    @Autowired
    private MarketListRepository marketListRepository;

    @Override
    public Page<Item> getAllItems(Pageable pageable) {
        try {
            UserDetails userDetails = SecurityUtils.getCurrentUserDetails();
            if (userDetails == null) throw new NotAuthenticatedException("Please, re-authenticate.");

            return itemRepository.findAllByUserEmailEquals(userDetails.getUsername(), pageable);
        } catch (UnexpectedErrorException exception) {
            throw new UnexpectedErrorException("An unexpected error occurred.");
        }
    }

    @Override
    public Page<Item> getItemsByName(String name, Pageable pageable) {
        try {
            UserDetails userDetails = SecurityUtils.getCurrentUserDetails();
            if (userDetails == null) throw new NotAuthenticatedException("Please, re-authenticate.");

            return itemRepository.findAllByUserEmailEqualsAndNameContaining(userDetails.getUsername(), name, pageable);
        } catch (UnexpectedErrorException exception) {
            throw new UnexpectedErrorException("An unexpected error occurred.");
        }
    }

    @Override
    public Item getItemById(Long id) {
        try {
            UserDetails userDetails = SecurityUtils.getCurrentUserDetails();
            if (userDetails == null) throw new NotAuthenticatedException("Please, re-authenticate.");

            return itemRepository.findAllByUserEmailEqualsAndIdEquals(userDetails.getUsername(), id)
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

            itemRepository.findByUserEmailEqualsAndNameEquals(userDetails.getUsername(), itemRequest.getName())
                    .ifPresent(item -> {
                        throw new ItemAlreadyExistsException("Item with the name: " + itemRequest.getName() + " already exists.");
                    });

            User user = userRepository.findByEmailEquals(userDetails.getUsername()).orElseThrow(() -> new UserNotFoundException("User with this email does not exist."));

            MeasuringUnit measuringUnit = measuringUnitRepository.findByUserEmailEqualsAndIdEquals(userDetails.getUsername(), itemRequest.getMeasuringUnitId())
                    .orElseThrow(() -> new MeasuringUnitNotFoundException("Measuring unit with this id: " + itemRequest.getMeasuringUnitId() + " does not exist."));

            Item savedItem = itemRepository.save(getItemToSave(user, measuringUnit, itemRequest));

            if (itemRequest.getQuantity() <= itemRequest.getLowLimit()) {
                marketListService.createMarketListItem(itemRequest);
            }

            return savedItem;
        } catch (UnexpectedErrorException exception) {
            throw new UnexpectedErrorException("An unexpected error occurred.");
        }
    }

    @Override
    public Item updateItem(Item item) {
        UserDetails userDetails = SecurityUtils.getCurrentUserDetails();
        if (userDetails == null) throw new NotAuthenticatedException("Please, re-authenticate.");

        if (item.getId() == null)
            throw new ItemNotFoundException("Please provide the \"id\" of the item in your request.");

        Item savedItem = itemRepository.findAllByUserEmailEqualsAndIdEquals(userDetails.getUsername(), item.getId())
                .orElseThrow(() -> new ItemNotFoundException("Item with id: " + item.getId() + " not found."));

        Optional<MarketList> optionalMarketList = marketListRepository.findByUserEmailEqualsAndNameEquals(userDetails.getUsername(), savedItem.getName());

        if (
                (
                        Objects.nonNull(item.getName()) ||
                                Objects.nonNull(item.getQuantity()) ||
                                Objects.nonNull(item.getLowLimit()) ||
                                Objects.nonNull(item.getPrice()) ||
                                Objects.nonNull(item.getCurrencyName()) ||
                                Objects.nonNull(item.getCurrencySymbol()) ||
                                Objects.nonNull(item.getNeedRestock())
                ) && optionalMarketList.isPresent()
        ) {
            MarketList marketListWithSameItemName = optionalMarketList.get();

            getMarketListToUpdate(savedItem, item, marketListWithSameItemName);

            if (!marketListWithSameItemName.getNeedRestock()) {
                marketListRepository.deleteById(marketListWithSameItemName.getId());
            } else {
                marketListRepository.save(marketListWithSameItemName);
            }
        }

        Item updatedItem = itemRepository.save(getItemToUpdate(savedItem, item));

        if (updatedItem.getNeedRestock() && optionalMarketList.isEmpty()) {
            marketListService.createMarketListItem(convertedItemRequestFromItem(updatedItem));
        }

        return updatedItem;
    }

    @Override
    public String deleteItem(Long id) {
        try {
            UserDetails userDetails = SecurityUtils.getCurrentUserDetails();
            if (userDetails == null) throw new NotAuthenticatedException("Please, re-authenticate.");

            Item itemToDelete = itemRepository.findAllByUserEmailEqualsAndIdEquals(userDetails.getUsername(), id)
                    .orElseThrow(() -> new ItemNotFoundException("Item with id: " + id + " not found."));

            marketListRepository.findByUserEmailEqualsAndNameEquals(userDetails.getUsername(), itemToDelete.getName())
                    .ifPresent(marketList -> marketListRepository.deleteById(marketList.getId()));

            itemRepository.deleteById(id);
            return "Deleted item with id: " + id;
        } catch (UnexpectedErrorException exception) {
            throw new UnexpectedErrorException("An unexpected error occurred.");
        }
    }

    public static Item getItemToSave(User user, MeasuringUnit measuringUnit, ItemRequest itemRequest) {
        return new Item(
                itemRequest.getName(),
                itemRequest.getQuantity(),
                itemRequest.getLowLimit(),
                itemRequest.getPrice(),
                itemRequest.getCurrencyName(),
                itemRequest.getCurrencySymbol(),
                itemRequest.getQuantity() <= itemRequest.getLowLimit(),
                user,
                measuringUnit
        );
    }

    public static Item getItemToUpdate(Item savedItem, Item item) {
        if (Objects.nonNull(item.getName())) {
            savedItem.setName(item.getName());
        }
        if (Objects.nonNull(item.getQuantity())) {
            savedItem.setQuantity(item.getQuantity());
        }
        if (Objects.nonNull(item.getLowLimit())) {
            savedItem.setLowLimit(item.getLowLimit());
        }
        if (Objects.nonNull(item.getPrice())) {
            savedItem.setPrice(item.getPrice());
        }
        if (Objects.nonNull(item.getCurrencyName())) {
            savedItem.setCurrencyName(item.getCurrencyName());
        }
        if (Objects.nonNull(item.getCurrencySymbol())) {
            savedItem.setCurrencySymbol(item.getCurrencySymbol());
        }
        if (Objects.nonNull(item.getNeedRestock())) {
            savedItem.setNeedRestock(item.getNeedRestock());
        } else {
            savedItem.setNeedRestock(savedItem.getQuantity() <= savedItem.getLowLimit());
        }

        return savedItem;
    }

    public static void getMarketListToUpdate(Item savedItem, Item item, MarketList marketListWithSameItemName) {
        if (Objects.nonNull(item.getName())) {
            marketListWithSameItemName.setName(item.getName());
        }
        if (Objects.nonNull(item.getLowLimit())) {
            marketListWithSameItemName.setLowLimit(item.getLowLimit());
        }
        if (Objects.nonNull(item.getPrice())) {
            marketListWithSameItemName.setPrice(item.getPrice());
        }
        if (Objects.nonNull(item.getCurrencyName())) {
            marketListWithSameItemName.setCurrencyName(item.getCurrencyName());
        }
        if (Objects.nonNull(item.getCurrencySymbol())) {
            marketListWithSameItemName.setCurrencySymbol(item.getCurrencySymbol());
        }
        if (Objects.nonNull(item.getNeedRestock())) {
            marketListWithSameItemName.setNeedRestock(item.getNeedRestock());
        } else {
            marketListWithSameItemName.setNeedRestock(savedItem.getQuantity() <= marketListWithSameItemName.getLowLimit());
        }
    }

    private ItemRequest convertedItemRequestFromItem(Item item) {
        return new ItemRequest(
                item.getName(),
                item.getQuantity(),
                item.getLowLimit(),
                item.getPrice(),
                item.getCurrencyName(),
                item.getCurrencySymbol(),
                item.getMeasuringUnit().getId()
        );
    }
}
