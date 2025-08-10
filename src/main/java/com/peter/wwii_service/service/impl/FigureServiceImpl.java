package com.peter.wwii_service.service.impl;

import com.peter.wwii_service.model.Country;
import com.peter.wwii_service.model.Figure;
import com.peter.wwii_service.model.Side;
import com.peter.wwii_service.repository.CountryRepository;
import com.peter.wwii_service.repository.FigureRepository;
import com.peter.wwii_service.service.FigureService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class FigureServiceImpl implements FigureService {

    private final FigureRepository figureRepository;
    private final CountryRepository countryRepository;

    @Override
    public List<Figure> getAllFigures() {
        log.info("Fetching all figures");
        return figureRepository.findAll();
    }

    @Override
    public Optional<Figure> getFigureById(Long id) {
        log.info("Fetching figure with id: {}", id);
        return figureRepository.findById(id);
    }

    @Override
    @Transactional
    public Figure createFigure(Figure figure, Long countryId) {
        log.info("Creating figure: {} for country id: {}", figure.getName(), countryId);

        // Find the country
        Country country = countryRepository.findById(countryId)
                .orElseThrow(() -> new RuntimeException("Country not found with id: " + countryId));

        // Set the country for the figure
        figure.setCountry(country);

        // Save the figure
        Figure savedFigure = figureRepository.save(figure);

        log.info("Figure created successfully with id: {}", savedFigure.getId());
        return savedFigure;
    }

    @Override
    @Transactional
    public Figure updateFigure(Long id, Figure updatedFigure) {
        log.info("Updating figure with id: {}", id);

        Figure existingFigure = figureRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Figure not found with id: " + id));

        // Update fields
        existingFigure.setName(updatedFigure.getName());
        existingFigure.setDescription(updatedFigure.getDescription());
        existingFigure.setSide(updatedFigure.getSide());

        Figure savedFigure = figureRepository.save(existingFigure);
        log.info("Figure updated successfully with id: {}", savedFigure.getId());

        return savedFigure;
    }

    @Override
    @Transactional
    public String deleteFigure(Long id) {
        log.info("Deleting figure with id: {}", id);

        if (!figureRepository.existsById(id)) {
            throw new RuntimeException("Figure not found with id: " + id);
        }

        figureRepository.deleteById(id);
        log.info("Figure deleted successfully with id: {}", id);
        return "Figure with id: " + id + " deleted successfully!";
    }

    @Override
    public List<Figure> getFiguresByCountry(Long countryId) {
        log.info("Fetching figures for country id: {}", countryId);
        return figureRepository.findByCountryId(countryId);
    }

    @Override
    public List<Figure> getFiguresBySide(Side side) {
        log.info("Fetching figures for side: {}", side);
        return figureRepository.findBySide(side);
    }
}