package com.peter.wwii_service.service;

import com.peter.wwii_service.model.Battle;

import java.util.List;

public interface BattleService {
    List<Battle> getAllBattles();
    Battle getBattleById(Long id);
    Battle createBattle(Battle battle, Long countryId);
    Battle updateBattle(Long id, Battle battle);
    void deleteBattle(Long id);
}