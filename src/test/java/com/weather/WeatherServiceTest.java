package com.weather;

import com.weather.dto.*;
import com.weather.service.GeocodingService;
import com.weather.service.WeatherClient;
import com.weather.service.WeatherService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WeatherServiceTest {

    @Mock
    private GeocodingService geocodingService;

    @Mock
    private WeatherClient weatherClient;

    @InjectMocks
    private WeatherService weatherService;

    @Test
    void testGetWeatherByCity_Metric() {
        LocationDto mockLocation = new LocationDto("London", "United Kingdom", "GB", "England", 51.5074, -0.1278, "Europe/London");
        when(geocodingService.resolveCity("London")).thenReturn(mockLocation);

        CurrentWeatherDto mockCurrent = new CurrentWeatherDto(
                20.0, 21.0, 60, 15.0, 1013.0, 5.0, 1, "Mainly Clear", "fa-cloud-sun", true, "2026-11-13T12:00"
        );
        DailyForecastDto mockDaily = new DailyForecastDto(
                "2026-11-13", "Today", 15.0, 22.0, 20, 1, "Mainly Clear", "fa-cloud-sun", "06:00", "18:00"
        );
        WeatherClient.RawWeatherData rawData = new WeatherClient.RawWeatherData(
                mockCurrent,
                Collections.singletonList(mockDaily),
                Collections.emptyList()
        );
        when(weatherClient.fetchWeather(anyDouble(), anyDouble())).thenReturn(rawData);

        WeatherReportDto report = weatherService.getWeatherByCity("London", "metric");

        assertNotNull(report);
        assertEquals("London", report.getLocation().getName());
        assertEquals(20.0, report.getCurrent().getTemperature());
        assertEquals("metric", report.getUnit());
        assertEquals(1, report.getDailyForecast().size());
        assertEquals(22.0, report.getDailyForecast().get(0).getMaxTemp());
    }

    @Test
    void testGetWeatherByCity_ImperialConversion() {
        LocationDto mockLocation = new LocationDto("Paris", "France", "FR", "Île-de-France", 48.8566, 2.3522, "Europe/Paris");
        when(geocodingService.resolveCity("Paris")).thenReturn(mockLocation);

        CurrentWeatherDto mockCurrent = new CurrentWeatherDto(
                0.0, 0.0, 70, 10.0, 1015.0, 3.0, 3, "Overcast", "fa-cloud", true, "2026-11-13T12:00"
        );
        WeatherClient.RawWeatherData rawData = new WeatherClient.RawWeatherData(
                mockCurrent,
                Collections.emptyList(),
                Collections.emptyList()
        );
        when(weatherClient.fetchWeather(anyDouble(), anyDouble())).thenReturn(rawData);

        WeatherReportDto report = weatherService.getWeatherByCity("Paris", "imperial");

        assertNotNull(report);
        assertEquals("imperial", report.getUnit());
        // 0°C converted to Fahrenheit should be 32°F
        assertEquals(32.0, report.getCurrent().getTemperature());
    }
}

