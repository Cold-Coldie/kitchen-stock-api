package com.coldie.kitchenstocks.measuringUnit.service;

import com.coldie.kitchenstocks.exception.MeasuringUnitAlreadyExistsException;
import com.coldie.kitchenstocks.exception.MeasuringUnitNotFoundException;
import com.coldie.kitchenstocks.measuringUnit.model.MeasuringUnit;
import com.coldie.kitchenstocks.measuringUnit.repository.MeasuringUnitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MeasuringUnitServiceImpl implements MeasuringUnitService {

    @Autowired
    private MeasuringUnitRepository measuringUnitRepository;


    @Override
    public List<MeasuringUnit> getAllMeasuringUnitsByUserId(Long userId) {
        return measuringUnitRepository.findByUserId(userId);
    }

    @Override
    public MeasuringUnit getMeasuringUnitByIdAndUserId(Long measuringUnitId, Long userId) {
        return measuringUnitRepository.findByIdAndUserId(measuringUnitId, userId).orElseThrow(() -> new MeasuringUnitNotFoundException("Measuring unit with id: " + measuringUnitId + " not found."));
    }

    @Override
    public MeasuringUnit createMeasuringUnit(MeasuringUnit measuringUnit) {
        Optional<MeasuringUnit> optionalMeasuringUnit = measuringUnitRepository.findByNameEqualsAndUserId(measuringUnit.getName(), measuringUnit.getUser().getId());

        if (optionalMeasuringUnit.isPresent()) {
            throw new MeasuringUnitAlreadyExistsException("Measuring Unit with the name: " + measuringUnit.getName() + " already exists.");
        }

        return measuringUnitRepository.save(measuringUnit);
    }
}
