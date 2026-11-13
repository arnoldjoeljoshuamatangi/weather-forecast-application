package com.weather.dto;

public class HourlyForecastDto {

    private String time;
    private double temperature;
    private int humidity;
    private int precipitationProbability;
    private int weatherCode;
    private String condition;
    private String icon;

    public HourlyForecastDto() {
    }

    public HourlyForecastDto(String time, double temperature, int humidity, int precipitationProbability,
                             int weatherCode, String condition, String icon) {
        this.time = time;
        this.temperature = temperature;
        this.humidity = humidity;
        this.precipitationProbability = precipitationProbability;
        this.weatherCode = weatherCode;
        this.condition = condition;
        this.icon = icon;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public int getHumidity() {
        return humidity;
    }

    public void setHumidity(int humidity) {
        this.humidity = humidity;
    }

    public int getPrecipitationProbability() {
        return precipitationProbability;
    }

    public void setPrecipitationProbability(int precipitationProbability) {
        this.precipitationProbability = precipitationProbability;
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
}

