package com.peter.wwii_service.service;

import com.peter.wwii_service.model.Vehicle;

import java.util.List;
import java.util.Optional;

public interface VehicleService {
    List<Vehicle> getAllVehicles();
    Vehicle getVehicleById(Long id);
    Vehicle createVehicle(Vehicle vehicle, Long countryId);
    Vehicle updateVehicle(Long id, Vehicle vehicle);
    String deleteVehicle(Long id);
}
