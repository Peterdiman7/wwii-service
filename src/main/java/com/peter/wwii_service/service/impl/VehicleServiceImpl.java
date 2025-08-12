package com.peter.wwii_service.service.impl;

import com.peter.wwii_service.model.Country;
import com.peter.wwii_service.model.Vehicle;
import com.peter.wwii_service.repository.CountryRepository;
import com.peter.wwii_service.repository.VehicleRepository;
import com.peter.wwii_service.service.VehicleService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class VehicleServiceImpl implements VehicleService {

    private final VehicleRepository vehicleRepository;
    private final CountryRepository countryRepository;

    @Override
    public List<Vehicle> getAllVehicles() {
        log.info("Fetching all vehicles");
        return vehicleRepository.findAll();
    }

    @Override
    public Vehicle getVehicleById(Long id) {
        log.info("Fetching vehicle with id: " + id);
        return vehicleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Vehicle with id: " + id + " not found!"));
    }

    @Override
    @Transactional
    public Vehicle createVehicle(Vehicle vehicle, Long countryId) {
        log.info("Creating vehicle: {}", vehicle.getName());

        // Validate basic fields
        if (vehicle.getName() == null || vehicle.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Name is required");
        }
        if (vehicle.getVehicleType() == null) {
            throw new IllegalArgumentException("Vehicle type is required");
        }

        // Find and validate country - this should come BEFORE setting it on vehicle
        Country country = countryRepository.findById(countryId)
                .orElseThrow(() -> new RuntimeException("Country with id: " + countryId + " not found!"));

        // Set the country on the vehicle
        vehicle.setCountry(country);

        // Save the vehicle
        Vehicle savedVehicle = vehicleRepository.save(vehicle);
        log.info("Vehicle created successfully with id: {}", savedVehicle.getId());

        return savedVehicle;
    }

    @Override
    @Transactional
    public Vehicle updateVehicle(Long id, Vehicle updatedVehicle) {
        log.info("Updating vehicle with id: {}", id);

        Vehicle existingVehicle = vehicleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Vehicle with id: " + id + " not found!"));

        existingVehicle.setName(updatedVehicle.getName());
        existingVehicle.setDescription(updatedVehicle.getDescription());
        existingVehicle.setVehicleType(updatedVehicle.getVehicleType());

        Vehicle savedVehicle = vehicleRepository.save(existingVehicle);

        log.info("Vehicle updated successfully with id: {}", savedVehicle.getId());

        return savedVehicle;
    }

    @Override
    @Transactional
    public void deleteVehicle(Long id) {
        Vehicle vehicle = vehicleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Vehicle with id: " + id + " not found!"));

        vehicleRepository.deleteById(id);
        log.info("Vehicle deleted successfully with id: {}", id);
    }
}