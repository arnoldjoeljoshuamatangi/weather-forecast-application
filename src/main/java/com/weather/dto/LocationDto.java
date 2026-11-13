package com.weather.dto;

public class LocationDto {

    private String name;
    private String country;
    private String countryCode;
    private String admin1; // State / Region
    private double latitude;
    private double longitude;
    private String timezone;

    public LocationDto() {
    }

    public LocationDto(String name, String country, String countryCode, String admin1, double latitude, double longitude, String timezone) {
        this.name = name;
        this.country = country;
        this.countryCode = countryCode;
        this.admin1 = admin1;
        this.latitude = latitude;
        this.longitude = longitude;
        this.timezone = timezone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getAdmin1() {
        return admin1;
    }

    public void setAdmin1(String admin1) {
        this.admin1 = admin1;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    public String getDisplayName() {
        StringBuilder sb = new StringBuilder(name);
        if (admin1 != null && !admin1.isBlank() && !admin1.equalsIgnoreCase(name)) {
            sb.append(", ").append(admin1);
        }
        if (country != null && !country.isBlank()) {
            sb.append(", ").append(country);
        }
        return sb.toString();
    }
}

