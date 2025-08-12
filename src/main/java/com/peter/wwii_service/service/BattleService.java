package com.peter.wwii_service.service;

import com.peter.wwii_service.model.Battle;

import java.util.List;

public interface BattleService {
    List<Battle> getAllBattles();
    Battle getBattleById(Long id);
    Battle createBattle(Battle battle, List<Long> countryIds);
    Battle updateBattle(Long id, Battle battle);
    Battle addCountryToBattle(Long battleId, Long countryId);
    Battle removeCountryFromBattle(Long battleId, Long countryId);
    List<Battle> getBattlesByCountry(Long countryId);
    void deleteBattle(Long id);
}