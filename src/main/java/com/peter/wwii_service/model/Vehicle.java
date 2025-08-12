package com.peter.wwii_service.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Entity
@Data
@Table(name = "vehicles")
public class Vehicle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Name is required!")
    private String name;

    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "country_id", referencedColumnName = "id")
    @JsonBackReference("country-vehicles")  // Match the named reference in Country
    private Country country;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "You need to enter valid Vehicle Type!")
    private VehicleType vehicleType;
}