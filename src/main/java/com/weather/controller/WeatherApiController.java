package com.weather.controller;

import com.weather.dto.*;
import com.weather.service.WeatherService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/weather")
@Tag(name = "Weather API", description = "Endpoints for fetching real-time weather, 5-day forecasts, and city geocoding")
@CrossOrigin(origins = "*")
public class WeatherApiController {

    private final WeatherService weatherService;

    public WeatherApiController(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    @GetMapping("/report")
    @Operation(summary = "Get full weather report", description = "Returns current weather, 5-day forecast, and 24-hour hourly trend for a city")
    public ResponseEntity<ApiResponse<WeatherReportDto>> getWeatherReport(
            @RequestParam(defaultValue = "London") String city,
            @RequestParam(defaultValue = "metric") String unit) {
        WeatherReportDto report = weatherService.getWeatherByCity(city, unit);
        return ResponseEntity.ok(ApiResponse.success(report));
    }

    @GetMapping("/current")
    @Operation(summary = "Get current weather", description = "Fetches real-time temperature, humidity, wind, and conditions")
    public ResponseEntity<ApiResponse<CurrentWeatherDto>> getCurrentWeather(
            @RequestParam(defaultValue = "London") String city,
            @RequestParam(defaultValue = "metric") String unit) {
        WeatherReportDto report = weatherService.getWeatherByCity(city, unit);
        return ResponseEntity.ok(ApiResponse.success(report.getCurrent()));
    }

    @GetMapping("/forecast")
    @Operation(summary = "Get 5-day forecast", description = "Returns daily high/low temperatures, precipitation probability, and weather conditions")
    public ResponseEntity<ApiResponse<List<DailyForecastDto>>> getForecast(
            @RequestParam(defaultValue = "London") String city,
            @RequestParam(defaultValue = "5") int days,
            @RequestParam(defaultValue = "metric") String unit) {
        WeatherReportDto report = weatherService.getWeatherByCity(city, unit);
        List<DailyForecastDto> daily = report.getDailyForecast();
        if (days > 0 && days < daily.size()) {
            daily = daily.subList(0, days);
        }
        return ResponseEntity.ok(ApiResponse.success(daily));
    }

    @GetMapping("/coordinates")
    @Operation(summary = "Get weather by GPS coordinates", description = "Fetches weather data for exact latitude and longitude coordinates")
    public ResponseEntity<ApiResponse<WeatherReportDto>> getWeatherByCoordinates(
            @RequestParam double lat,
            @RequestParam double lon,
            @RequestParam(defaultValue = "metric") String unit) {
        WeatherReportDto report = weatherService.getWeatherByCoordinates(lat, lon, unit);
        return ResponseEntity.ok(ApiResponse.success(report));
    }

    @GetMapping("/search")
    @Operation(summary = "Search cities for autocomplete", description = "Returns list of matching cities and coordinates for live search input")
    public ResponseEntity<ApiResponse<List<LocationDto>>> searchCities(
            @RequestParam String query) {
        List<LocationDto> locations = weatherService.searchCities(query);
        return ResponseEntity.ok(ApiResponse.success(locations));
    }
}

