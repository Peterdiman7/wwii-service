package com.peter.wwii_service.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Entity
@Data
@Table(name = "countries")
public class Country {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "figure_id", referencedColumnName = "id")
    @JsonBackReference
    private Figure figure;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "Side must be either ALLIES, AXIS or NEUTRAL!")
    private Side side;
}
