package com.coldie.kitchenstocks.measuringUnit.controller;

import com.coldie.kitchenstocks.exception.UnexpectedErrorException;
import com.coldie.kitchenstocks.measuringUnit.model.MeasuringUnit;
import com.coldie.kitchenstocks.measuringUnit.service.MeasuringUnitService;
import com.coldie.kitchenstocks.user.model.User;
import com.coldie.kitchenstocks.user.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
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
    public ResponseEntity<List<MeasuringUnit>> getAllMeasuringUnits() {
        return new ResponseEntity<List<MeasuringUnit>>(measuringUnitService.getAllMeasuringUnits(), HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<MeasuringUnit> createUser(@Valid @RequestBody MeasuringUnit measuringUnit) {
        return new ResponseEntity<MeasuringUnit>(measuringUnitService.createMeasuringUnit(measuringUnit), HttpStatus.CREATED);
    }
}
