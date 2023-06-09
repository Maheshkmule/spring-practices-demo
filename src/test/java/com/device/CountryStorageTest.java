package com.device;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.springpageable.storage.CountryStorage;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.List;

import static com.device.mock.Constants.*;
import static com.springpageable.storage.CountryStorage.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CountryStorageTest {

    private static CountryStorage countryStorage;

    @BeforeAll
    static void beforeAll() throws IOException {
        countryStorage = new CountryStorage();
    }

    @Test
    void testThat_getCountriesByCountryCodes_returnsTheExpectedCountries() {
        // Act
        var result = countryStorage.getCountriesByCountryCodes(List.of(COUNTRY_BG_KEY, COUNTRY_DE_KEY));

        // Assert
        assertFalse(result.isEmpty());
        assertEquals(2, result.size());
        assertEquals(2, result.stream()
                .filter(country -> country.getName().equals(COUNTRY_BG_VALUE) || country.getName().equals(COUNTRY_DE_VALUE))
                .count());
    }

    @Test
    void testThat_getCountriesByCountryCodes_returnsAllCountries_whenTheFilterIsEmpty() {
        // Act
        var result = countryStorage.getCountriesByCountryCodes(List.of());

        // Assert
        assertEquals(ALL_COUNTRIES_SIZE, result.size());
    }

    @Test
    void testThat_getCountriesByCountryNameStartsWith_returnsTheExpectedCountries() {
        // Act
        var result = countryStorage.getCountriesByCountryNameStartsWith(COUNTRY_BG_VALUE);

        // Assert
        assertFalse(result.isEmpty());
        assertTrue(result.stream().anyMatch(country -> country.getName().equals(COUNTRY_BG_VALUE)));
    }

    @Test
    void testThat_getCountriesByCountryNameStartsWith_returnsAllCountries_whenTheFilterIsNull() {
        // Act
        var result = countryStorage.getCountriesByCountryNameStartsWith(null);

        // Assert
        assertEquals(ALL_COUNTRIES_SIZE, result.size());
    }

    @Test
    void testThat_getCountryByCountryCode_returnsCountry_whenTheFilterIsValid() {
        // Act
        var result = countryStorage.getCountryByCountryCode(COUNTRY_BG_KEY);

        // Assert
        assertNotNull(result);
        assertEquals(COUNTRY_BG_VALUE, result.getName());
    }

    @Test
    void testThat_getCountryByCountryCode_returnsNull_whenTheFilterIsNull() {
        // Act
        var result = countryStorage.getCountryByCountryCode(null);

        // Assert
        assertNull(result);
    }

    @Test
    void testThat_getCountryByCountryCode_returnsNull_whenTheCountryDoesNotExist() {
        // Act
        var result = countryStorage.getCountryByCountryCode(NON_EXISTING_COUNTRY_CODE);

        // Assert
        assertNull(result);
    }

    @Test
    void testThat_areAllCountryCodesSupported_validatesProperly() {
        // Act & Assert
        assertTrue(countryStorage.areAllCountryCodesSupported(List.of(COUNTRY_BG_KEY, COUNTRY_DE_KEY)));
        assertFalse(
                countryStorage.areAllCountryCodesSupported(List.of(COUNTRY_BG_KEY, NON_EXISTING_COUNTRY_CODE, COUNTRY_DE_KEY)));
    }

    @Test
    void testThat_getCountriesByCountryNameStartsWith_returnsAllCountries_whenTheFilterIsEmpty() {
        // Act
        var result = countryStorage.getCountriesByCountryNameStartsWith(EMPTY_STRING);

        // Assert
        assertEquals(ALL_COUNTRIES_SIZE, result.size());
    }

    @Test
    void testThat_getCountriesInCERegion_returnsCountries() {
        // Act
        var result = countryStorage.getCountriesInCERegion();

        // Assert
        assertNotNull(result);
        assertFalse(result.isEmpty());
    }

    @Test
    void testThat_getCountriesInFrenchOverseasRegion_returnsCountries() {
        // Act
        var result = countryStorage.getCountriesInFrenchOverseasRegion();

        // Assert
        assertNotNull(result);
        assertFalse(result.isEmpty());
    }
}
