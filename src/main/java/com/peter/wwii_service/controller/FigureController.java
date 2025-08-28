package com.peter.wwii_service.controller;

import com.peter.wwii_service.model.Figure;
import com.peter.wwii_service.model.Side;
import com.peter.wwii_service.service.FigureService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/figures")
@RequiredArgsConstructor
@Slf4j
public class FigureController {

    private final FigureService figureService;

    @GetMapping
    public ResponseEntity<?> getAllFigures() {
        try {
            log.info("GET /api/figures - Fetching all figures");
            List<Figure> figures = figureService.getAllFigures();
            log.info("Successfully fetched {} figures", figures.size());
            return ResponseEntity.ok(figures);
        } catch (Exception e) {
            log.error("Error fetching all figures: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to fetch figures: " + e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getFigureById(@PathVariable Long id) {
        try {
            log.info("GET /api/figures/{} - Fetching figure by id", id);
            Optional<Figure> figure = figureService.getFigureById(id);

            if (figure.isPresent()) {
                return ResponseEntity.ok(figure.get());
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            log.error("Error fetching figure by id {}: {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to fetch figure: " + e.getMessage()));
        }
    }

    @PostMapping("/country/{countryId}")
    public ResponseEntity<Figure> createFigure(
            @PathVariable Long countryId,
            @Valid @RequestBody Figure figure) {

        Figure savedFigure = figureService.createFigure(figure, countryId);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedFigure);
    }


    @PostMapping
    public ResponseEntity<?> createFigureWithCountryInBody(@Valid @RequestBody CreateFigureRequest request) {
        try {
            log.info("POST /api/figures - Creating figure: {} for country id: {}",
                    request.getName(), request.getCountryId());

            // Validate required fields
            if (request.getName() == null || request.getName().trim().isEmpty()) {
                log.warn("Figure creation failed: Name is required");
                return ResponseEntity.badRequest().body(Map.of("error", "Name is required"));
            }
            if (request.getSide() == null) {
                log.warn("Figure creation failed: Side is required");
                return ResponseEntity.badRequest().body(Map.of("error", "Side is required"));
            }
            if (request.getCountryId() == null) {
                log.warn("Figure creation failed: Country ID is required");
                return ResponseEntity.badRequest().body(Map.of("error", "Country ID is required"));
            }

            // Create Figure object from request
            Figure figure = new Figure();
            figure.setName(request.getName());
            figure.setDescription(request.getDescription());
            figure.setSide(request.getSide());
            figure.setImgUrl(request.getImgUrl());

            Figure savedFigure = figureService.createFigure(figure, request.getCountryId());
            log.info("Figure created successfully with id: {}", savedFigure.getId());
            return ResponseEntity.status(HttpStatus.CREATED).body(savedFigure);

        } catch (RuntimeException e) {
            log.error("Error creating figure: {}", e.getMessage());
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            log.error("Unexpected error creating figure: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to create figure: " + e.getMessage()));
        }
    }

    // DTO class for creating figures with country ID in body
    public static class CreateFigureRequest {
        @jakarta.validation.constraints.NotBlank(message = "Name is required")
        private String name;

        private String description;

        @jakarta.validation.constraints.NotNull(message = "Side is required")
        private Side side;

        @jakarta.validation.constraints.NotNull(message = "Image is required")
        private String imgUrl;

        @jakarta.validation.constraints.NotNull(message = "Country ID is required")
        private Long countryId;

        // Getters and setters
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }

        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }

        public Side getSide() { return side; }
        public void setSide(Side side) { this.side = side; }

        public String getImgUrl() { return imgUrl; }
        public void setImgUrl(String imgUrl) { this.imgUrl = imgUrl; }

        public Long getCountryId() { return countryId; }
        public void setCountryId(Long countryId) { this.countryId = countryId; }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateFigure(@PathVariable Long id,
                                          @Valid @RequestBody Figure figure) {
        try {
            log.info("PUT /api/figures/{} - Updating figure", id);
            Figure updatedFigure = figureService.updateFigure(id, figure);
            return ResponseEntity.ok(updatedFigure);
        } catch (RuntimeException e) {
            log.error("Error updating figure: {}", e.getMessage());
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            log.error("Unexpected error updating figure: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to update figure: " + e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteFigure(@PathVariable Long id) {
        try {
            String message = figureService.deleteFigure(id);
            Map<String, String> response = new HashMap<>();
            response.put("message", message);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            log.error("Error deleting figure: {}", e.getMessage());
            Map<String, String> response = new HashMap<>();
            response.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping("/country/{countryId}")
    public ResponseEntity<?> getFiguresByCountry(@PathVariable Long countryId) {
        try {
            log.info("GET /api/figures/country/{} - Fetching figures by country", countryId);
            List<Figure> figures = figureService.getFiguresByCountry(countryId);
            return ResponseEntity.ok(figures);
        } catch (Exception e) {
            log.error("Error fetching figures by country: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to fetch figures: " + e.getMessage()));
        }
    }

    @GetMapping("/side/{side}")
    public ResponseEntity<?> getFiguresBySide(@PathVariable Side side) {
        try {
            log.info("GET /api/figures/side/{} - Fetching figures by side", side);
            List<Figure> figures = figureService.getFiguresBySide(side);
            return ResponseEntity.ok(figures);
        } catch (Exception e) {
            log.error("Error fetching figures by side: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to fetch figures: " + e.getMessage()));
        }
    }
}