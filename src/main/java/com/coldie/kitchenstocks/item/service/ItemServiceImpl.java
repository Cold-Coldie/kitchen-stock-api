package com.coldie.kitchenstocks.item.service;

import com.coldie.kitchenstocks.config.SecurityUtils;
import com.coldie.kitchenstocks.exception.UnexpectedErrorException;
import com.coldie.kitchenstocks.item.exception.ItemAlreadyExistsException;
import com.coldie.kitchenstocks.item.exception.ItemNotFoundException;
import com.coldie.kitchenstocks.item.model.Item;
import com.coldie.kitchenstocks.item.request.ItemRequest;
import com.coldie.kitchenstocks.item.respository.ItemRepository;
import com.coldie.kitchenstocks.marketlist.model.MarketList;
import com.coldie.kitchenstocks.marketlist.service.MarketListService;
import com.coldie.kitchenstocks.measuringUnit.model.MeasuringUnit;
import com.coldie.kitchenstocks.measuringUnit.repository.MeasuringUnitRepository;
import com.coldie.kitchenstocks.user.exception.UserNotFoundException;
import com.coldie.kitchenstocks.user.model.User;
import com.coldie.kitchenstocks.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

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

    @Override
    public Item createItem(ItemRequest itemRequest) {
        try {
            UserDetails userDetails = SecurityUtils.getCurrentUserDetails();
            if (userDetails == null) throw new UserNotFoundException("User with this email does not exist.");

            Optional<Item> optionalItem = itemRepository.findByUserEmailEqualsAndNameEquals(userDetails.getUsername(), itemRequest.getName());

            if (optionalItem.isPresent())
                throw new ItemAlreadyExistsException("Item with the name: " + itemRequest.getName() + " already exists.");

            User user = userRepository.findByEmailEquals(userDetails.getUsername()).orElseThrow(() -> new UserNotFoundException("User with this email does not exist."));

            MeasuringUnit measuringUnit = measuringUnitRepository.findByUserEmailEqualsAndIdEquals(userDetails.getUsername(), itemRequest.getMeasuringUnitId())
                    .orElseThrow(() -> new ItemNotFoundException("Measuring unit with this id: " + itemRequest.getMeasuringUnitId() + " does not exist."));

            if (itemRequest.getQuantity() <= itemRequest.getLowLimit()) {
                marketListService.createMarketListItem(itemRequest);
            }

            return itemRepository.save(getItemToSave(user, measuringUnit, itemRequest));
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
