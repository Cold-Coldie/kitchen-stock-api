package com.coldie.kitchenstocks.measuringUnit.service;

import com.coldie.kitchenstocks.measuringUnit.model.MeasuringUnit;

import java.util.List;

public interface MeasuringUnitService {

    List<MeasuringUnit> getAllMeasuringUnits();


    MeasuringUnit createMeasuringUnit(MeasuringUnit measuringUnit);
}
