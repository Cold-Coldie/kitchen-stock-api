package com.coldie.kitchenstocks.measuringUnit.service;

import com.coldie.kitchenstocks.measuringUnit.model.MeasuringUnit;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MeasuringUnitService {

    Page<MeasuringUnit> getAllMeasuringUnits(Pageable pageable);

    Page<MeasuringUnit> getMeasuringUnitsByName(String name, Pageable pageable);

    MeasuringUnit getMeasuringUnitById(Long id);

    MeasuringUnit createMeasuringUnit(MeasuringUnit measuringUnit);

    MeasuringUnit updateMeasuringUnit(MeasuringUnit measuringUnit);

    String deleteMeasuringUnitById(Long id);
}
