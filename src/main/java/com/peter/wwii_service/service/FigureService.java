package com.peter.wwii_service.service;

import com.peter.wwii_service.model.Figure;
import com.peter.wwii_service.model.Side;

import java.util.List;
import java.util.Optional;

public interface FigureService {
    List<Figure> getAllFigures();
    Optional<Figure> getFigureById(Long id);
    Figure createFigure(Figure figure, Long countryId);
    Figure updateFigure(Long id, Figure figure);
    String deleteFigure(Long id);
    List<Figure> getFiguresByCountry(Long countryId);
    List<Figure> getFiguresBySide(Side side);
}