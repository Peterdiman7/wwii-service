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
@Slf4j
public class VehicleController {

    private final VehicleService vehicleService;

    public VehicleController(VehicleService vehicleService) {
        this.vehicleService = vehicleService;
    }

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

    @PutMapping("/{id}")
    public ResponseEntity<Vehicle> updateVehicle(
            @PathVariable("id") Long id,
            @RequestBody Vehicle vehicle
    ) {
        Vehicle updatedVehicle = vehicleService.updateVehicle(id, vehicle);

        return ResponseEntity.ok(updatedVehicle);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteVehicle(@PathVariable("id") Long id) {
        vehicleService.deleteVehicle(id);

        return ResponseEntity.ok("Vehicle with id: " + id + " deleted successfully!");
    }
}
