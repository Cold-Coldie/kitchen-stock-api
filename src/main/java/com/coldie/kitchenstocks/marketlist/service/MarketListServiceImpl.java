package com.coldie.kitchenstocks.marketlist.service;

import com.coldie.kitchenstocks.config.SecurityUtils;
import com.coldie.kitchenstocks.config.exception.NotAuthenticatedException;
import com.coldie.kitchenstocks.exception.UnexpectedErrorException;
import com.coldie.kitchenstocks.item.exception.ItemNotFoundException;
import com.coldie.kitchenstocks.item.model.Item;
import com.coldie.kitchenstocks.item.request.ItemRequest;
import com.coldie.kitchenstocks.marketlist.exception.MarketListItemAlreadyExistsException;
import com.coldie.kitchenstocks.marketlist.model.MarketList;
import com.coldie.kitchenstocks.marketlist.repository.MarketListRepository;
import com.coldie.kitchenstocks.measuringUnit.exception.MeasuringUnitNotFoundException;
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
public class MarketListServiceImpl implements MarketListService {

    @Autowired
    private MarketListRepository marketListRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MeasuringUnitRepository measuringUnitRepository;

    @Override
    public MarketList createMarketListItem(ItemRequest itemRequest) {
        try {
            UserDetails userDetails = SecurityUtils.getCurrentUserDetails();
            if (userDetails == null) throw new NotAuthenticatedException("Please, re-authenticate.");

            Optional<MarketList> optionalMarketList = marketListRepository.findByUserEmailEqualsAndNameEquals(userDetails.getUsername(), itemRequest.getName());

            if (optionalMarketList.isPresent()) {
                throw new MarketListItemAlreadyExistsException("Market list item with the name: " + itemRequest.getName() + " already exists.");
            }

            User user = userRepository.findByEmailEquals(userDetails.getUsername()).orElseThrow(() -> new UserNotFoundException("User with this email does not exist."));

            MeasuringUnit measuringUnit = measuringUnitRepository.findByUserEmailEqualsAndIdEquals(userDetails.getUsername(), itemRequest.getMeasuringUnitId())
                    .orElseThrow(() -> new MeasuringUnitNotFoundException("Measuring unit with this id: " + itemRequest.getMeasuringUnitId() + " does not exist."));

            return marketListRepository.save(getMarketListItemToSave(user, measuringUnit, itemRequest));
        } catch (UnexpectedErrorException exception) {
            throw new UnexpectedErrorException("An unexpected error occurred.");
        }
    }

    public static MarketList getMarketListItemToSave(User user, MeasuringUnit measuringUnit, ItemRequest itemRequest) {
        return new MarketList(
                itemRequest.getName(),
                1,
                itemRequest.getLowLimit(),
                itemRequest.getPrice(),
                itemRequest.getCurrencyName(),
                itemRequest.getCurrencySymbol(),
                itemRequest.getQuantity() <= itemRequest.getLowLimit(),
                user,
                measuringUnit
        );
    }
}
