package com.coldie.kitchenstocks.measuringUnit.controller;

import com.coldie.kitchenstocks.exception.UnexpectedErrorException;
import com.coldie.kitchenstocks.measuringUnit.model.MeasuringUnit;
import com.coldie.kitchenstocks.measuringUnit.service.MeasuringUnitService;
import com.coldie.kitchenstocks.user.model.User;
import com.coldie.kitchenstocks.user.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/measuring-unit")
public class MeasuringUnitController {

    @Autowired
    private MeasuringUnitService measuringUnitService;

    @Autowired
    private UserService userService;

    @GetMapping("")
    public ResponseEntity<Page<MeasuringUnit>> getAllMeasuringUnits(
            @RequestParam(name = "name", required = false) String name,
            @PageableDefault(page = 0, size = 10, sort = {"id"}, direction = Sort.Direction.DESC) Pageable pageable
    ) {
        if (name != null) {
            return new ResponseEntity<Page<MeasuringUnit>>(measuringUnitService.getMeasuringUnitsByName(name, pageable), HttpStatus.OK);
        } else {
            return new ResponseEntity<Page<MeasuringUnit>>(measuringUnitService.getAllMeasuringUnits(pageable), HttpStatus.OK);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<MeasuringUnit> getMeasuringUnitById(@PathVariable("id") Long id) {
        return new ResponseEntity<MeasuringUnit>(measuringUnitService.getMeasuringUnitById(id), HttpStatus.OK);
    }

    @PostMapping("")
    public ResponseEntity<MeasuringUnit> createMeasuringUnit(@Valid @RequestBody MeasuringUnit measuringUnit) {
        return new ResponseEntity<MeasuringUnit>(measuringUnitService.createMeasuringUnit(measuringUnit), HttpStatus.CREATED);
    }

    @PutMapping("")
    public ResponseEntity<MeasuringUnit> updateMeasuringUnit(@RequestBody MeasuringUnit measuringUnit) {
        return new ResponseEntity<MeasuringUnit>(measuringUnitService.updateMeasuringUnit(measuringUnit), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteMeasuringUnitById(@PathVariable("id") Long id) {
        return new ResponseEntity<String>(measuringUnitService.deleteMeasuringUnitById(id), HttpStatus.OK);
    }
}
