package com.peter.wwii_service.controller;

import com.peter.wwii_service.model.Battle;
import com.peter.wwii_service.service.BattleService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/battles")
@Slf4j
public class BattleController {

    private final BattleService battleService;

    public BattleController(BattleService battleService) {
        this.battleService = battleService;
    }

    @GetMapping
    public ResponseEntity<List<Battle>> getAllBattles() {
        List<Battle> battles = battleService.getAllBattles();
        return ResponseEntity.ok(battles);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Battle> getBattleById(@PathVariable("id") Long id) {
        Battle battle = battleService.getBattleById(id);
        return ResponseEntity.ok(battle);
    }

    // Create battle with multiple countries
    @PostMapping
    public ResponseEntity<?> createBattle(@Valid @RequestBody CreateBattleRequest request) {
        try {
            log.info("POST /api/battles - Creating battle: {} with countries: {}",
                    request.getName(), request.getCountryIds());

            // Validate required fields
            if (request.getName() == null || request.getName().trim().isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("error", "Name is required"));
            }
            if (request.getLocation() == null || request.getLocation().trim().isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("error", "Location is required"));
            }
            if (request.getCountryIds() == null || request.getCountryIds().isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("error", "At least one country is required"));
            }

            // Create Battle object from request
            Battle battle = new Battle();
            battle.setName(request.getName());
            battle.setLocation(request.getLocation());
            battle.setImgUrl(request.getImgUrl());

            Battle savedBattle = battleService.createBattle(battle, request.getCountryIds());
            log.info("Battle created successfully with id: {}", savedBattle.getId());
            return ResponseEntity.status(HttpStatus.CREATED).body(savedBattle);

        } catch (RuntimeException e) {
            log.error("Error creating battle: {}", e.getMessage());
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            log.error("Unexpected error creating battle: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to create battle: " + e.getMessage()));
        }
    }

    // Legacy endpoint for backward compatibility (single country)
    @PostMapping("/country/{countryId}")
    public ResponseEntity<Battle> createBattleWithSingleCountry(
            @Valid @RequestBody Battle battle,
            @PathVariable Long countryId
    ) {
        Battle newBattle = battleService.createBattle(battle, List.of(countryId));
        return ResponseEntity.ok(newBattle);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Battle> updateBattle(
            @PathVariable("id") Long id,
            @RequestBody Battle battle
    ) {
        Battle updatedBattle = battleService.updateBattle(id, battle);
        return ResponseEntity.ok(updatedBattle);
    }

    // Add country to existing battle
    @PostMapping("/{battleId}/countries/{countryId}")
    public ResponseEntity<?> addCountryToBattle(
            @PathVariable Long battleId,
            @PathVariable Long countryId
    ) {
        try {
            Battle updatedBattle = battleService.addCountryToBattle(battleId, countryId);
            return ResponseEntity.ok(updatedBattle);
        } catch (RuntimeException e) {
            log.error("Error adding country to battle: {}", e.getMessage());
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // Remove country from battle
    @DeleteMapping("/{battleId}/countries/{countryId}")
    public ResponseEntity<?> removeCountryFromBattle(
            @PathVariable Long battleId,
            @PathVariable Long countryId
    ) {
        try {
            Battle updatedBattle = battleService.removeCountryFromBattle(battleId, countryId);
            return ResponseEntity.ok(updatedBattle);
        } catch (RuntimeException e) {
            log.error("Error removing country from battle: {}", e.getMessage());
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // Get battles by country
    @GetMapping("/country/{countryId}")
    public ResponseEntity<List<Battle>> getBattlesByCountry(@PathVariable Long countryId) {
        List<Battle> battles = battleService.getBattlesByCountry(countryId);
        return ResponseEntity.ok(battles);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteBattle(@PathVariable("id") Long id) {
        battleService.deleteBattle(id);
        return ResponseEntity.ok("Battle with id: " + id + " deleted successfully!");
    }

    // DTO class for creating battles with multiple countries
    public static class CreateBattleRequest {
        @jakarta.validation.constraints.NotBlank(message = "Name is required")
        private String name;

        @jakarta.validation.constraints.NotBlank(message = "Location is required")
        private String location;

        private String imgUrl;

        @jakarta.validation.constraints.NotNull(message = "Country IDs are required")
        @jakarta.validation.constraints.Size(min = 1, message = "At least one country is required")
        private List<Long> countryIds;

        // Getters and setters
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }

        public String getLocation() { return location; }
        public void setLocation(String location) { this.location = location; }

        public String getImgUrl() { return imgUrl; }
        public void setImgUrl(String imgUrl) { this.imgUrl = imgUrl; }

        public List<Long> getCountryIds() { return countryIds; }
        public void setCountryIds(List<Long> countryIds) { this.countryIds = countryIds; }
    }
}