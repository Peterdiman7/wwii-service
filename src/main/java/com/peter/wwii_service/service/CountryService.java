package com.peter.wwii_service.service;

import com.peter.wwii_service.model.Country;

import java.util.List;
import java.util.Optional;

public interface CountryService {
    List<Country> getAllCountries();
    Optional<Country> getCountryById(Long id);
    Country createCountry(Country country);
    Country updateCountry(Long id, Country country);
    String deleteCountry(Long id);
}
