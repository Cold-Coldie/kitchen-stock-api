package com.coldie.kitchenstocks.measuringUnit.service;

import com.coldie.kitchenstocks.measuringUnit.model.MeasuringUnit;

import java.util.List;

public interface MeasuringUnitService {

    List<MeasuringUnit> getAllMeasuringUnitsByUserId(Long userId);

    MeasuringUnit getMeasuringUnitByIdAndUserId(Long measuringUnitId, Long userId);

    MeasuringUnit createMeasuringUnit(MeasuringUnit measuringUnit);
}
