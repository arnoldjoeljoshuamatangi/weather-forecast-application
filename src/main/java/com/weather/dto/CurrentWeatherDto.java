package com.weather.dto;

public class CurrentWeatherDto {

    private double temperature;
    private double feelsLike;
    private int humidity;
    private double windSpeed;
    private double pressure;
    private double uvIndex;
    private int weatherCode;
    private String condition;
    private String icon;
    private boolean day;
    private String observationTime;

    public CurrentWeatherDto() {
    }

    public CurrentWeatherDto(double temperature, double feelsLike, int humidity, double windSpeed,
                             double pressure, double uvIndex, int weatherCode, String condition,
                             String icon, boolean day, String observationTime) {
        this.temperature = temperature;
        this.feelsLike = feelsLike;
        this.humidity = humidity;
        this.windSpeed = windSpeed;
        this.pressure = pressure;
        this.uvIndex = uvIndex;
        this.weatherCode = weatherCode;
        this.condition = condition;
        this.icon = icon;
        this.day = day;
        this.observationTime = observationTime;
    }

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public double getFeelsLike() {
        return feelsLike;
    }

    public void setFeelsLike(double feelsLike) {
        this.feelsLike = feelsLike;
    }

    public int getHumidity() {
        return humidity;
    }

    public void setHumidity(int humidity) {
        this.humidity = humidity;
    }

    public double getWindSpeed() {
        return windSpeed;
    }

    public void setWindSpeed(double windSpeed) {
        this.windSpeed = windSpeed;
    }

    public double getPressure() {
        return pressure;
    }

    public void setPressure(double pressure) {
        this.pressure = pressure;
    }

    public double getUvIndex() {
        return uvIndex;
    }

    public void setUvIndex(double uvIndex) {
        this.uvIndex = uvIndex;
    }

    public int getWeatherCode() {
        return weatherCode;
    }

    public void setWeatherCode(int weatherCode) {
        this.weatherCode = weatherCode;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public boolean isDay() {
        return day;
    }

    public void setDay(boolean day) {
        this.day = day;
    }

    public String getObservationTime() {
        return observationTime;
    }

    public void setObservationTime(String observationTime) {
        this.observationTime = observationTime;
    }
}

