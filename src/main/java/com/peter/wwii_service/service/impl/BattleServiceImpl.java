package com.peter.wwii_service.service.impl;

import com.peter.wwii_service.model.Battle;
import com.peter.wwii_service.model.Country;
import com.peter.wwii_service.model.Vehicle;
import com.peter.wwii_service.repository.BattleRepository;
import com.peter.wwii_service.repository.CountryRepository;
import com.peter.wwii_service.repository.VehicleRepository;
import com.peter.wwii_service.service.BattleService;
import com.peter.wwii_service.service.VehicleService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class BattleServiceImpl implements BattleService {

    private final BattleRepository battleRepository;
    private final CountryRepository countryRepository;

    @Override
    public List<Battle> getAllBattles() {
        log.info("Fetching all vehicles");
        return battleRepository.findAll();
    }

    @Override
    public Battle getBattleById(Long id) {
        log.info("Fetching vehicle with id: " + id);
        return battleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Vehicle with id: " + id + " not found!"));
    }

    @Override
    @Transactional
    public Battle createBattle(Battle battle, Long countryId) {
        log.info("Creating vehicle: {}", battle.getName());

        // Validate basic fields
        if (battle.getName() == null || battle.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Name is required");
        }
        if (battle.getLocation() == null) {
            throw new IllegalArgumentException("Battle location is required");
        }

        // Find and validate country - this should come BEFORE setting it on vehicle
        Country country = countryRepository.findById(countryId)
                .orElseThrow(() -> new RuntimeException("Country with id: " + countryId + " not found!"));

        // Set the country on the vehicle
        battle.setCountry(country);

        // Save the vehicle
        Battle savedBattle = battleRepository.save(battle);
        log.info("Battle created successfully with id: {}", savedBattle.getId());

        return savedBattle;
    }

    @Override
    @Transactional
    public Battle updateBattle(Long id, Battle updatedBattle) {
        log.info("Updating battle with id: {}", id);

        Battle existingBattle = battleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Battle with id: " + id + " not found!"));

        existingBattle.setName(existingBattle.getName());
        existingBattle.setLocation(updatedBattle.getLocation());
        existingBattle.setImgUrl(existingBattle.getImgUrl());

        Battle savedBattle = battleRepository.save(existingBattle);

        log.info("Battle updated successfully with id: {}", savedBattle.getId());

        return savedBattle;
    }

    @Override
    @Transactional
    public void deleteBattle(Long id) {
        Battle battle = battleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Vehicle with id: " + id + " not found!"));

        battleRepository.deleteById(id);
        log.info("Vehicle deleted successfully with id: {}", id);
    }
}