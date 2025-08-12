package com.peter.wwii_service.controller;

import com.peter.wwii_service.model.Battle;
import com.peter.wwii_service.service.BattleService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @PostMapping("/country/{countryId}")
    public ResponseEntity<Battle> createBattle(
            @Valid @RequestBody Battle battle,
            @PathVariable Long countryId
    ) {
        Battle newBattle = battleService.createBattle(battle, countryId);

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

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteBattle(@PathVariable("id") Long id) {
        battleService.deleteBattle(id);

        return ResponseEntity.ok("Battle with id: " + id + " deleted successfully!");
    }
}
