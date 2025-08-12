package com.peter.wwii_service.repository;

import com.peter.wwii_service.model.Battle;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BattleRepository extends JpaRepository<Battle, Long> {
}
