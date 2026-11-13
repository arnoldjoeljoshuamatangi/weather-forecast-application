package com.weather.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.weather.config.CacheConfig;
import com.weather.dto.LocationDto;
import com.weather.exception.CityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.*;

@Service
public class GeocodingService {

    private static final Logger log = LoggerFactory.getLogger(GeocodingService.class);
    private static final String GEOCODING_API_URL = "https://geocoding-api.open-meteo.com/v1/search";

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    // Fallback dictionary for popular cities
    private static final Map<String, LocationDto> STATIC_LOCATIONS = new HashMap<>();

    static {
        STATIC_LOCATIONS.put("london", new LocationDto("London", "United Kingdom", "GB", "England", 51.5085, -0.1257, "Europe/London"));
        STATIC_LOCATIONS.put("new york", new LocationDto("New York", "United States", "US", "New York", 40.7128, -74.0060, "America/New_York"));
        STATIC_LOCATIONS.put("tokyo", new LocationDto("Tokyo", "Japan", "JP", "Tokyo", 35.6895, 139.6917, "Asia/Tokyo"));
        STATIC_LOCATIONS.put("paris", new LocationDto("Paris", "France", "FR", "Île-de-France", 48.8534, 2.3488, "Europe/Paris"));
        STATIC_LOCATIONS.put("sydney", new LocationDto("Sydney", "Australia", "AU", "New South Wales", -33.8688, 151.2093, "Australia/Sydney"));
        STATIC_LOCATIONS.put("mumbai", new LocationDto("Mumbai", "India", "IN", "Maharashtra", 19.0760, 72.8777, "Asia/Kolkata"));
        STATIC_LOCATIONS.put("berlin", new LocationDto("Berlin", "Germany", "DE", "Berlin", 52.5244, 13.4105, "Europe/Berlin"));
        STATIC_LOCATIONS.put("singapore", new LocationDto("Singapore", "Singapore", "SG", "", 1.2897, 103.8501, "Asia/Singapore"));
        STATIC_LOCATIONS.put("dubai", new LocationDto("Dubai", "United Arab Emirates", "AE", "Dubai", 25.0772, 55.3093, "Asia/Dubai"));
        STATIC_LOCATIONS.put("san francisco", new LocationDto("San Francisco", "United States", "US", "California", 37.7749, -122.4194, "America/Los_Angeles"));
    }

    public GeocodingService(RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    @Cacheable(value = CacheConfig.GEOCODING_CACHE, key = "#cityName.toLowerCase()")
    public LocationDto resolveCity(String cityName) {
        if (cityName == null || cityName.isBlank()) {
            throw new IllegalArgumentException("City name cannot be empty");
        }

        String trimmed = cityName.trim();
        List<LocationDto> locations = searchLocations(trimmed);
        if (locations.isEmpty()) {
            // Check fallback dictionary
            LocationDto fallback = STATIC_LOCATIONS.get(trimmed.toLowerCase());
            if (fallback != null) {
                return fallback;
            }
            throw new CityNotFoundException("City '" + cityName + "' could not be found");
        }
        return locations.get(0);
    }

    public List<LocationDto> searchLocations(String query) {
        if (query == null || query.trim().length() < 2) {
            return Collections.emptyList();
        }

        try {
            String url = UriComponentsBuilder.fromHttpUrl(GEOCODING_API_URL)
                    .queryParam("name", query.trim())
                    .queryParam("count", 5)
                    .queryParam("language", "en")
                    .queryParam("format", "json")
                    .build().toUriString();

            String response = restTemplate.getForObject(url, String.class);
            if (response != null) {
                JsonNode root = objectMapper.readTree(response);
                JsonNode results = root.path("results");
                if (results.isArray() && results.size() > 0) {
                    List<LocationDto> list = new ArrayList<>();
                    for (JsonNode item : results) {
                        list.add(new LocationDto(
                                item.path("name").asText(),
                                item.path("country").asText(""),
                                item.path("country_code").asText(""),
                                item.path("admin1").asText(""),
                                item.path("latitude").asDouble(),
                                item.path("longitude").asDouble(),
                                item.path("timezone").asText("UTC")
                        ));
                    }
                    return list;
                }
            }
        } catch (Exception e) {
            log.warn("External geocoding failed for '{}', checking local cache/fallback: {}", query, e.getMessage());
        }

        // Fallback filter
        String lowerQuery = query.toLowerCase();
        List<LocationDto> fallbackList = new ArrayList<>();
        STATIC_LOCATIONS.forEach((key, loc) -> {
            if (key.contains(lowerQuery) || loc.getCountry().toLowerCase().contains(lowerQuery)) {
                fallbackList.add(loc);
            }
        });
        return fallbackList;
    }
}

