package com.weather.dto;

import java.time.LocalDateTime;
import java.util.List;

public class WeatherReportDto {

    private LocationDto location;
    private CurrentWeatherDto current;
    private List<DailyForecastDto> dailyForecast;
    private List<HourlyForecastDto> hourlyForecast;
    private String unit; // "metric" (°C, km/h) or "imperial" (°F, mph)
    private LocalDateTime fetchedAt;
    private boolean cached;

    public WeatherReportDto() {
        this.fetchedAt = LocalDateTime.now();
    }

    public WeatherReportDto(LocationDto location, CurrentWeatherDto current,
                            List<DailyForecastDto> dailyForecast, List<HourlyForecastDto> hourlyForecast,
                            String unit, boolean cached) {
        this.location = location;
        this.current = current;
        this.dailyForecast = dailyForecast;
        this.hourlyForecast = hourlyForecast;
        this.unit = unit;
        this.fetchedAt = LocalDateTime.now();
        this.cached = cached;
    }

    public LocationDto getLocation() {
        return location;
    }

    public void setLocation(LocationDto location) {
        this.location = location;
    }

    public CurrentWeatherDto getCurrent() {
        return current;
    }

    public void setCurrent(CurrentWeatherDto current) {
        this.current = current;
    }

    public List<DailyForecastDto> getDailyForecast() {
        return dailyForecast;
    }

    public void setDailyForecast(List<DailyForecastDto> dailyForecast) {
        this.dailyForecast = dailyForecast;
    }

    public List<HourlyForecastDto> getHourlyForecast() {
        return hourlyForecast;
    }

    public void setHourlyForecast(List<HourlyForecastDto> hourlyForecast) {
        this.hourlyForecast = hourlyForecast;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public LocalDateTime getFetchedAt() {
        return fetchedAt;
    }

    public void setFetchedAt(LocalDateTime fetchedAt) {
        this.fetchedAt = fetchedAt;
    }

    public boolean isCached() {
        return cached;
    }

    public void setCached(boolean cached) {
        this.cached = cached;
    }
}

