package com.peter.wwii_service.repository;

import com.peter.wwii_service.model.Figure;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FigureRepository extends JpaRepository<Figure, Long> {

    // Find figures by country ID
    List<Figure> findByCountryId(Long countryId);

    // Find figures by side
    List<Figure> findBySide(com.peter.wwii_service.model.Side side);

    // Custom query to find figures with their country information
    @Query("SELECT f FROM Figure f JOIN FETCH f.country WHERE f.id = :id")
    Figure findByIdWithCountry(@Param("id") Long id);
}