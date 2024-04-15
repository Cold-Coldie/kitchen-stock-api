package com.coldie.kitchenstocks.measuringUnit.repository;

import com.coldie.kitchenstocks.measuringUnit.model.MeasuringUnit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MeasuringUnitRepository extends JpaRepository<MeasuringUnit, Long> {

    List<MeasuringUnit> findByUserId(Long userId);

    Optional<MeasuringUnit> findByIdAndUserId(Long measuringUnitId, Long userId);

    Optional<MeasuringUnit> findByNameEqualsAndUserId(String name, Long userId);
}
