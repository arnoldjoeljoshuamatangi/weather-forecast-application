package com.weather.dto;

public class DailyForecastDto {

    private String date;
    private String dayOfWeek;
    private double minTemp;
    private double maxTemp;
    private int precipitationProbability;
    private int weatherCode;
    private String condition;
    private String icon;
    private String sunrise;
    private String sunset;

    public DailyForecastDto() {
    }

    public DailyForecastDto(String date, String dayOfWeek, double minTemp, double maxTemp,
                            int precipitationProbability, int weatherCode, String condition,
                            String icon, String sunrise, String sunset) {
        this.date = date;
        this.dayOfWeek = dayOfWeek;
        this.minTemp = minTemp;
        this.maxTemp = maxTemp;
        this.precipitationProbability = precipitationProbability;
        this.weatherCode = weatherCode;
        this.condition = condition;
        this.icon = icon;
        this.sunrise = sunrise;
        this.sunset = sunset;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(String dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public double getMinTemp() {
        return minTemp;
    }

    public void setMinTemp(double minTemp) {
        this.minTemp = minTemp;
    }

    public double getMaxTemp() {
        return maxTemp;
    }

    public void setMaxTemp(double maxTemp) {
        this.maxTemp = maxTemp;
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

    public String getSunrise() {
        return sunrise;
    }

    public void setSunrise(String sunrise) {
        this.sunrise = sunrise;
    }

    public String getSunset() {
        return sunset;
    }

    public void setSunset(String sunset) {
        this.sunset = sunset;
    }
}

