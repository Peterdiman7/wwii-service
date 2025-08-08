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

    @Transactional
    public String deleteCountry(Long id) {
        log.info("Deleting country with id: {}", id);

        if (!countryRepository.existsById(id)) {
            System.out.println("Country not found with id: " + id);
        }

        countryRepository.deleteById(id);
        log.info("Country deleted successfully with id: {}", id);
        return "Country with id: " + id + " deleted successfully!";
    }
}
