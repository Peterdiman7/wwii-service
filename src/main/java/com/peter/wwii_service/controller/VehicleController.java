package com.peter.wwii_service.controller;

import com.peter.wwii_service.model.Vehicle;
import com.peter.wwii_service.service.VehicleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vehicles")
@RequiredArgsConstructor
@Slf4j
public class VehicleController {

    private final VehicleService vehicleService;

    @GetMapping
    public ResponseEntity<List<Vehicle>> getAllVehicles() {
        List<Vehicle> vehicles = vehicleService.getAllVehicles();

        return ResponseEntity.ok(vehicles);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Vehicle> getVehicleById(@PathVariable("id") Long id) {
        Vehicle vehicle = vehicleService.getVehicleById(id);

        return ResponseEntity.ok(vehicle);
    }

    @PostMapping("/country/{countryId}")
    public ResponseEntity<Vehicle> createVehicle(
            @Valid @RequestBody Vehicle vehicle,
            @PathVariable Long countryId
            ) {
        Vehicle newVehicle = vehicleService.createVehicle(vehicle, countryId);

        return ResponseEntity.ok(newVehicle);
    }
}
