package com.peter.wwii_service.service.impl;

import com.peter.wwii_service.model.Country;
import com.peter.wwii_service.repository.CountryRepository;
import com.peter.wwii_service.service.CountryService;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class CountryServiceImpl implements CountryService {
    private final com.peter.wwii_service.repository.CountryRepository countryRepository;

    public CountryServiceImpl(CountryRepository countryRepository) {
        this.countryRepository = countryRepository;
    }

    public List<Country> getAllCountries() {
        return countryRepository.findAll();
    }

    public Optional<Country> getCountryById(Long id) {
        return countryRepository.findById(id);
    }

    public Country createCountry(Country country) {
        return countryRepository.save(country);
    }

    public Country updateCountry(Long id, Country country) {
        Country existingCountry = countryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Country not found with id: " + id));

        existingCountry.setName(country.getName());
        existingCountry.setDescription(country.getDescription());
        existingCountry.setSide(country.getSide());
        existingCountry.setImgUrl(country.getImgUrl());

        return countryRepository.save(existingCountry);
    }

    @Transactional
    public String deleteCountry(Long id) {
        Country existingCountry = countryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Country with id: " + id + " not found!"));

        countryRepository.delete(existingCountry);

        return "Country with id: " + id + " deleted successfully!";
    }
}
