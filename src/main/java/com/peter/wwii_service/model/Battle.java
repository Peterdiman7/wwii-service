package com.peter.wwii_service.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Table(name = "battles")
public class Battle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Name is required")
    private String name;

    private String location;

    private String imgUrl;

    // Many-to-Many relationship with Country
    @ManyToMany(mappedBy = "battles", fetch = FetchType.LAZY)
    @JsonIgnoreProperties({"battles", "figures", "vehicles"})  // Prevent circular references
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<Country> countries = new ArrayList<>();

    // Helper methods for managing the relationship
    public void addCountry(Country country) {
        if (!countries.contains(country)) {
            countries.add(country);
            country.getBattles().add(this);
        }
    }

    public void removeCountry(Country country) {
        countries.remove(country);
        country.getBattles().remove(this);
    }
}