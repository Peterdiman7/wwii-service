package com.peter.wwii_service.controller;

import com.peter.wwii_service.model.Country;
import com.peter.wwii_service.service.CountryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/countries")
@RequiredArgsConstructor
@Slf4j
public class CountryController {

    private final CountryService countryService;

    @GetMapping
    public ResponseEntity<?> getAllCountries() {
        try {
            System.out.println("GET /api/countries - Fetching all countries");
            List<Country> country = countryService.getAllCountries();
            System.out.printf("Successfully fetched %d country items", country.size());
            return ResponseEntity.ok(country);
        } catch (Exception e) {
            System.out.printf("Error fetching all countries %s", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to fetch countries: " + e.getMessage()));
        }
    }


    @GetMapping("/{id}")
    public ResponseEntity<?> getCountryById(@PathVariable Long id) {
        log.info("GET /api/countries/{} - Fetching user by id", id);
        Optional<Country> country = countryService.getCountryById(id);

        if (country.isPresent()) {
            return ResponseEntity.ok(country);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping(consumes = "application/json")
    public ResponseEntity<?> createCountry(@RequestBody Country country) {
        try {
            System.out.printf("POST /api/countries - Creating countries: %s", country.getName());

            // Validate required fields
            if (country.getName() == null || country.getName().trim().isEmpty()) {
                System.out.println("Country creation failed: Name is required");
                return ResponseEntity.badRequest().body(Map.of("error", "Name is required"));
            }
            if (country.getSide() == null) {
                System.out.println("Country creation failed: Side is required");
                return ResponseEntity.badRequest().body(Map.of("error", "Side is required"));
            }

            Country savedCountry = countryService.createCountry(country);
            System.out.printf("Country created successfully with id: %d", savedCountry.getId());
            return ResponseEntity.status(HttpStatus.CREATED).body(savedCountry);

        } catch (Exception e) {
            System.out.printf("Error creating country %s", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to create country: " + e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Country> updateCountry(
            @PathVariable("id") Long id,
            @RequestBody Country country) {

        Country updatedCountry = countryService.updateCountry(id, country);
        return ResponseEntity.ok(updatedCountry);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteCountry(@PathVariable("id") Long id) {
        String message = countryService.deleteCountry(id);
        Map<String, String> response = new HashMap<>();
        response.put("message", message);
        return ResponseEntity.ok(response);
    }
}