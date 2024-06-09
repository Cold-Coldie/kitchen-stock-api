package com.coldie.kitchenstocks.measuringUnit.service;

import com.coldie.kitchenstocks.config.SecurityUtils;
import com.coldie.kitchenstocks.config.exception.NotAuthenticatedException;
import com.coldie.kitchenstocks.exception.InvalidRequest;
import com.coldie.kitchenstocks.exception.UnexpectedErrorException;
import com.coldie.kitchenstocks.item.model.Item;
import com.coldie.kitchenstocks.item.respository.ItemRepository;
import com.coldie.kitchenstocks.measuringUnit.exception.MeasuringUnitAlreadyExistsException;
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
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class MeasuringUnitServiceImpl implements MeasuringUnitService {

    @Autowired
    private MeasuringUnitRepository measuringUnitRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ItemRepository itemRepository;


    @Override
    public Page<MeasuringUnit> getAllMeasuringUnits(Pageable pageable) {
        try {
            UserDetails userDetails = SecurityUtils.getCurrentUserDetails();
            if (userDetails == null) throw new NotAuthenticatedException("Please, re-authenticate.");

            return measuringUnitRepository.findAllByUser_EmailEquals(userDetails.getUsername(), pageable);
        } catch (UnexpectedErrorException exception) {
            throw new UnexpectedErrorException("An unexpected error occurred.");
        }
    }

    @Override
    public Page<MeasuringUnit> getMeasuringUnitsByName(String name, Pageable pageable) {
        try {
            UserDetails userDetails = SecurityUtils.getCurrentUserDetails();
            if (userDetails == null) throw new NotAuthenticatedException("Please, re-authenticate.");

            return measuringUnitRepository.findAllByUser_EmailEqualsAndNameContaining(userDetails.getUsername(), name, pageable);
        } catch (UnexpectedErrorException exception) {
            throw new UnexpectedErrorException("An unexpected error occurred.");
        }
    }

    @Override
    public MeasuringUnit getMeasuringUnitById(Long id) {
        try {
            UserDetails userDetails = SecurityUtils.getCurrentUserDetails();
            if (userDetails == null) throw new NotAuthenticatedException("Please, re-authenticate.");

            return measuringUnitRepository.findByUser_EmailEqualsAndIdEquals(userDetails.getUsername(), id)
                    .orElseThrow(() -> new MeasuringUnitNotFoundException("Measuring unit with id: " + id + " not found."));
        } catch (UnexpectedErrorException exception) {
            throw new UnexpectedErrorException("An unexpected error occurred.");
        }
    }

    @Override
    public MeasuringUnit createMeasuringUnit(MeasuringUnit measuringUnit) {
        try {
            UserDetails userDetails = SecurityUtils.getCurrentUserDetails();
            if (userDetails == null) throw new NotAuthenticatedException("Please, re-authenticate.");

            measuringUnitRepository
                    .findByUser_EmailEqualsAndNameEquals(userDetails.getUsername(), measuringUnit.getName())
                    .ifPresent(measuringUnit1 -> {
                        throw new MeasuringUnitAlreadyExistsException("Measuring unit with the name: " + measuringUnit.getName() + " already exists.");
                    });

            User user = userRepository.findByEmailEquals(userDetails.getUsername())
                    .orElseThrow(() -> new UserNotFoundException("User with this email does not exist."));

            measuringUnit.setUser(user);
            measuringUnitRepository.save(measuringUnit);

            return measuringUnit;
        } catch (UnexpectedErrorException exception) {
            throw new UnexpectedErrorException("An unexpected error occurred.");
        }
    }

    @Override
    public MeasuringUnit updateMeasuringUnit(MeasuringUnit measuringUnit) {
        try {
            UserDetails userDetails = SecurityUtils.getCurrentUserDetails();
            if (userDetails == null) throw new NotAuthenticatedException("Please, re-authenticate.");

            if (measuringUnit.getId() == null)
                throw new InvalidRequest("Please provide the \"id\" of the item in your request.");

            MeasuringUnit savedMeasuringUnit = measuringUnitRepository.findByUser_EmailEqualsAndIdEquals(userDetails.getUsername(), measuringUnit.getId())
                    .orElseThrow(() -> new MeasuringUnitNotFoundException("Measuring unit with id: " + measuringUnit.getId() + " not found."));

            if (Objects.nonNull(measuringUnit.getName())) {
                savedMeasuringUnit.setName(measuringUnit.getName());
            }

            return measuringUnitRepository.save(savedMeasuringUnit);
        } catch (UnexpectedErrorException exception) {
            throw new UnexpectedErrorException("An unexpected error occurred.");
        }
    }

    @Override
    @Transactional
    public String deleteMeasuringUnitById(Long id) {
        try {
            UserDetails userDetails = SecurityUtils.getCurrentUserDetails();
            if (userDetails == null) throw new NotAuthenticatedException("Please, re-authenticate.");

            measuringUnitRepository.findByUser_EmailEqualsAndIdEquals(userDetails.getUsername(), id)
                    .ifPresentOrElse(measuringUnit -> safelyDeleteMeasuringUnitById(measuringUnit.getId(), itemRepository, measuringUnitRepository, userDetails.getUsername()),
                            () -> {
                                throw new MeasuringUnitNotFoundException("Measuring unit with id: " + id + " not found.");
                            }
                    );

            return "Deleted measuring unit with id: " + id;
        } catch (UnexpectedErrorException exception) {
            throw new UnexpectedErrorException("An unexpected error occurred.");
        }
    }

    private static void safelyDeleteMeasuringUnitById(Long measuringUnitId, ItemRepository itemRepository, MeasuringUnitRepository measuringUnitRepository, String email) {
        List<Item> items = itemRepository.findAllByUser_EmailEqualsAndMeasuringUnit_IdEquals(email, measuringUnitId);

        if (!items.isEmpty()) {
            itemRepository.deleteAllByUser_EmailEqualsAndMeasuringUnit_IdEquals(email, measuringUnitId);
        }

        measuringUnitRepository.deleteById(measuringUnitId);
    }
}
