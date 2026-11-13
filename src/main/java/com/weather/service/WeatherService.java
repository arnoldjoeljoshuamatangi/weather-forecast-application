package com.weather.service;

import com.weather.config.CacheConfig;
import com.weather.dto.*;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class WeatherService {

    private final GeocodingService geocodingService;
    private final WeatherClient weatherClient;

    public WeatherService(GeocodingService geocodingService, WeatherClient weatherClient) {
        this.geocodingService = geocodingService;
        this.weatherClient = weatherClient;
    }

    @Cacheable(value = CacheConfig.WEATHER_CACHE, key = "#cityName.toLowerCase() + '-' + #unit")
    public WeatherReportDto getWeatherByCity(String cityName, String unit) {
        LocationDto location = geocodingService.resolveCity(cityName);
        return buildReport(location, unit, false);
    }

    public WeatherReportDto getWeatherByCoordinates(double latitude, double longitude, String unit) {
        LocationDto location = new LocationDto(
                "Current Location",
                "",
                "",
                "",
                latitude,
                longitude,
                "UTC"
        );
        return buildReport(location, unit, false);
    }

    public List<LocationDto> searchCities(String query) {
        return geocodingService.searchLocations(query);
    }

    private WeatherReportDto buildReport(LocationDto location, String unit, boolean cached) {
        String effectiveUnit = (unit != null && unit.equalsIgnoreCase("imperial")) ? "imperial" : "metric";
        boolean isImperial = "imperial".equalsIgnoreCase(effectiveUnit);

        WeatherClient.RawWeatherData raw = weatherClient.fetchWeather(location.getLatitude(), location.getLongitude());

        CurrentWeatherDto current = raw.current;
        List<DailyForecastDto> daily = raw.daily;
        List<HourlyForecastDto> hourly = raw.hourly;

        if (isImperial) {
            current.setTemperature(round(toFahrenheit(current.getTemperature())));
            current.setFeelsLike(round(toFahrenheit(current.getFeelsLike())));
            current.setWindSpeed(round(toMph(current.getWindSpeed())));

            daily = daily.stream().map(d -> new DailyForecastDto(
                    d.getDate(),
                    d.getDayOfWeek(),
                    round(toFahrenheit(d.getMinTemp())),
                    round(toFahrenheit(d.getMaxTemp())),
                    d.getPrecipitationProbability(),
                    d.getWeatherCode(),
                    d.getCondition(),
                    d.getIcon(),
                    d.getSunrise(),
                    d.getSunset()
            )).collect(Collectors.toList());

            hourly = hourly.stream().map(h -> new HourlyForecastDto(
                    h.getTime(),
                    round(toFahrenheit(h.getTemperature())),
                    h.getHumidity(),
                    h.getPrecipitationProbability(),
                    h.getWeatherCode(),
                    h.getCondition(),
                    h.getIcon()
            )).collect(Collectors.toList());
        }

        return new WeatherReportDto(location, current, daily, hourly, effectiveUnit, cached);
    }

    private double toFahrenheit(double c) {
        return (c * 9.0 / 5.0) + 32.0;
    }

    private double toMph(double kmh) {
        return kmh * 0.621371;
    }

    private double round(double val) {
        return Math.round(val * 10.0) / 10.0;
    }
}

