package com.coldie.kitchenstocks.measuringUnit.service;

import com.coldie.kitchenstocks.config.SecurityUtils;
import com.coldie.kitchenstocks.config.exception.NotAuthenticatedException;
import com.coldie.kitchenstocks.exception.UnexpectedErrorException;
import com.coldie.kitchenstocks.measuringUnit.exception.MeasuringUnitAlreadyExistsException;
import com.coldie.kitchenstocks.measuringUnit.exception.MeasuringUnitNotFoundException;
import com.coldie.kitchenstocks.measuringUnit.model.MeasuringUnit;
import com.coldie.kitchenstocks.measuringUnit.repository.MeasuringUnitRepository;
import com.coldie.kitchenstocks.user.exception.UserNotFoundException;
import com.coldie.kitchenstocks.user.model.User;
import com.coldie.kitchenstocks.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MeasuringUnitServiceImpl implements MeasuringUnitService {

    @Autowired
    private MeasuringUnitRepository measuringUnitRepository;

    @Autowired
    private UserRepository userRepository;


    @Override
    public List<MeasuringUnit> getAllMeasuringUnits() {
        try {
            UserDetails userDetails = SecurityUtils.getCurrentUserDetails();
            if (userDetails == null) throw new NotAuthenticatedException("Please, re-authenticate.");

            return measuringUnitRepository.findAllByUserEmailEquals(userDetails.getUsername());
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
                    .findByUserEmailEqualsAndNameEquals(userDetails.getUsername(), measuringUnit.getName())
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
}
