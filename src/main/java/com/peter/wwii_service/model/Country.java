package com.peter.wwii_service.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Table(name = "countries")
public class Country {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Name is required")
    private String name;

    private String description;

    // One Country can have many Figures
    @OneToMany(mappedBy = "country", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<Figure> figures = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    @NotNull(message = "Side must be either ALLIES, AXIS or NEUTRAL!")
    private Side side;

    public void addFigure(Figure figure) {
        figures.add(figure);
        figure.setCountry(this);
    }

    public void removeFigure(Figure figure) {
        figures.remove(figure);
        figure.setCountry(null);
    }
}