package com.coldie.kitchenstocks.measuringUnit.repository;

import com.coldie.kitchenstocks.measuringUnit.model.MeasuringUnit;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MeasuringUnitRepository extends JpaRepository<MeasuringUnit, Long> {

    Page<MeasuringUnit> findAllByUserEmailEquals(String email, Pageable pageable);

    Page<MeasuringUnit> findAllByUserEmailEqualsAndNameContaining(String email, String name, Pageable pageable);

    Optional<MeasuringUnit> findByUserEmailEqualsAndIdEquals(String email, Long id);

    Optional<MeasuringUnit> findByUserEmailEqualsAndNameEquals(String email, String name);
}
