package com.peter.wwii_service.repository;

import com.peter.wwii_service.model.Battle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BattleRepository extends JpaRepository<Battle, Long> {

    // Find battles by country ID
    @Query("SELECT b FROM Battle b JOIN b.countries c WHERE c.id = :countryId")
    List<Battle> findByCountryId(@Param("countryId") Long countryId);

    // Find battle with countries loaded
    @Query("SELECT b FROM Battle b LEFT JOIN FETCH b.countries WHERE b.id = :id")
    Optional<Battle> findByIdWithCountries(@Param("id") Long id);

    // Find battles that involve multiple countries (optional - for analytics)
    @Query("SELECT b FROM Battle b WHERE SIZE(b.countries) > 1")
    List<Battle> findBattlesWithMultipleCountries();
}