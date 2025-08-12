package com.peter.wwii_service.service.impl;

import com.peter.wwii_service.model.Battle;
import com.peter.wwii_service.model.Country;
import com.peter.wwii_service.repository.BattleRepository;
import com.peter.wwii_service.repository.CountryRepository;
import com.peter.wwii_service.service.BattleService;
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
        log.info("Fetching all battles");
        return battleRepository.findAll();
    }

    @Override
    public Battle getBattleById(Long id) {
        log.info("Fetching battle with id: {}", id);
        return battleRepository.findByIdWithCountries(id)
                .orElseThrow(() -> new RuntimeException("Battle with id: " + id + " not found!"));
    }

    @Override
    @Transactional
    public Battle createBattle(Battle battle, List<Long> countryIds) {
        log.info("Creating battle: {} with countries: {}", battle.getName(), countryIds);

        // Validate basic fields
        if (battle.getName() == null || battle.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Name is required");
        }
        if (battle.getLocation() == null || battle.getLocation().trim().isEmpty()) {
            throw new IllegalArgumentException("Battle location is required");
        }
        if (countryIds == null || countryIds.isEmpty()) {
            throw new IllegalArgumentException("At least one country is required");
        }

        // Save the battle first
        Battle savedBattle = battleRepository.save(battle);

        // Find and add countries
        for (Long countryId : countryIds) {
            Country country = countryRepository.findById(countryId)
                    .orElseThrow(() -> new RuntimeException("Country with id: " + countryId + " not found!"));
            savedBattle.addCountry(country);
        }

        // Save again to persist the relationships
        savedBattle = battleRepository.save(savedBattle);
        log.info("Battle created successfully with id: {} and {} countries",
                savedBattle.getId(), savedBattle.getCountries().size());

        return savedBattle;
    }

    @Override
    @Transactional
    public Battle updateBattle(Long id, Battle updatedBattle) {
        log.info("Updating battle with id: {}", id);

        Battle existingBattle = battleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Battle with id: " + id + " not found!"));

        // Update basic fields
        existingBattle.setName(updatedBattle.getName());
        existingBattle.setLocation(updatedBattle.getLocation());
        existingBattle.setImgUrl(updatedBattle.getImgUrl());

        Battle savedBattle = battleRepository.save(existingBattle);
        log.info("Battle updated successfully with id: {}", savedBattle.getId());

        return savedBattle;
    }

    @Override
    @Transactional
    public Battle addCountryToBattle(Long battleId, Long countryId) {
        log.info("Adding country {} to battle {}", countryId, battleId);

        Battle battle = battleRepository.findByIdWithCountries(battleId)
                .orElseThrow(() -> new RuntimeException("Battle with id: " + battleId + " not found!"));

        Country country = countryRepository.findById(countryId)
                .orElseThrow(() -> new RuntimeException("Country with id: " + countryId + " not found!"));

        battle.addCountry(country);
        Battle savedBattle = battleRepository.save(battle);

        log.info("Country added to battle successfully");
        return savedBattle;
    }

    @Override
    @Transactional
    public Battle removeCountryFromBattle(Long battleId, Long countryId) {
        log.info("Removing country {} from battle {}", countryId, battleId);

        Battle battle = battleRepository.findByIdWithCountries(battleId)
                .orElseThrow(() -> new RuntimeException("Battle with id: " + battleId + " not found!"));

        Country country = countryRepository.findById(countryId)
                .orElseThrow(() -> new RuntimeException("Country with id: " + countryId + " not found!"));

        battle.removeCountry(country);

        // Check if battle still has countries after removal
        if (battle.getCountries().isEmpty()) {
            log.warn("Battle {} has no countries after removal. Consider deleting it.", battleId);
        }

        Battle savedBattle = battleRepository.save(battle);
        log.info("Country removed from battle successfully");
        return savedBattle;
    }

    @Override
    public List<Battle> getBattlesByCountry(Long countryId) {
        log.info("Fetching battles for country: {}", countryId);
        return battleRepository.findByCountryId(countryId);
    }

    @Override
    @Transactional
    public void deleteBattle(Long id) {
        log.info("Deleting battle with id: {}", id);

        Battle battle = battleRepository.findByIdWithCountries(id)
                .orElseThrow(() -> new RuntimeException("Battle with id: " + id + " not found!"));

        // Clear all country relationships before deleting
        battle.getCountries().clear();
        battleRepository.save(battle);

        battleRepository.deleteById(id);
        log.info("Battle deleted successfully with id: {}", id);
    }
}