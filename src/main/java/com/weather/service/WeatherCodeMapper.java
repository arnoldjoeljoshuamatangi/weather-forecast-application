package com.weather.service;

import java.util.HashMap;
import java.util.Map;

public class WeatherCodeMapper {

    public static class WeatherMeta {
        private final String condition;
        private final String dayIcon;
        private final String nightIcon;

        public WeatherMeta(String condition, String dayIcon, String nightIcon) {
            this.condition = condition;
            this.dayIcon = dayIcon;
            this.nightIcon = nightIcon;
        }

        public String getCondition() {
            return condition;
        }

        public String getDayIcon() {
            return dayIcon;
        }

        public String getNightIcon() {
            return nightIcon;
        }
    }

    private static final Map<Integer, WeatherMeta> CODE_MAP = new HashMap<>();

    static {
        CODE_MAP.put(0, new WeatherMeta("Clear Sky", "fa-sun", "fa-moon"));
        CODE_MAP.put(1, new WeatherMeta("Mainly Clear", "fa-cloud-sun", "fa-cloud-moon"));
        CODE_MAP.put(2, new WeatherMeta("Partly Cloudy", "fa-cloud-sun", "fa-cloud-moon"));
        CODE_MAP.put(3, new WeatherMeta("Overcast", "fa-cloud", "fa-cloud"));
        CODE_MAP.put(45, new WeatherMeta("Foggy", "fa-smog", "fa-smog"));
        CODE_MAP.put(48, new WeatherMeta("Depositing Rime Fog", "fa-smog", "fa-smog"));
        CODE_MAP.put(51, new WeatherMeta("Light Drizzle", "fa-cloud-rain", "fa-cloud-rain"));
        CODE_MAP.put(53, new WeatherMeta("Moderate Drizzle", "fa-cloud-rain", "fa-cloud-rain"));
        CODE_MAP.put(55, new WeatherMeta("Dense Drizzle", "fa-cloud-showers-heavy", "fa-cloud-showers-heavy"));
        CODE_MAP.put(61, new WeatherMeta("Slight Rain", "fa-cloud-rain", "fa-cloud-rain"));
        CODE_MAP.put(63, new WeatherMeta("Moderate Rain", "fa-cloud-showers-heavy", "fa-cloud-showers-heavy"));
        CODE_MAP.put(65, new WeatherMeta("Heavy Rain", "fa-cloud-showers-heavy", "fa-cloud-showers-heavy"));
        CODE_MAP.put(71, new WeatherMeta("Slight Snow Fall", "fa-snowflake", "fa-snowflake"));
        CODE_MAP.put(73, new WeatherMeta("Moderate Snow Fall", "fa-snowflake", "fa-snowflake"));
        CODE_MAP.put(75, new WeatherMeta("Heavy Snow Fall", "fa-snowflake", "fa-snowflake"));
        CODE_MAP.put(77, new WeatherMeta("Snow Grains", "fa-snowflake", "fa-snowflake"));
        CODE_MAP.put(80, new WeatherMeta("Slight Rain Showers", "fa-cloud-sun-rain", "fa-cloud-moon-rain"));
        CODE_MAP.put(81, new WeatherMeta("Moderate Rain Showers", "fa-cloud-showers-heavy", "fa-cloud-showers-heavy"));
        CODE_MAP.put(82, new WeatherMeta("Violent Rain Showers", "fa-cloud-showers-heavy", "fa-cloud-showers-heavy"));
        CODE_MAP.put(85, new WeatherMeta("Slight Snow Showers", "fa-snowflake", "fa-snowflake"));
        CODE_MAP.put(86, new WeatherMeta("Heavy Snow Showers", "fa-snowflake", "fa-snowflake"));
        CODE_MAP.put(95, new WeatherMeta("Thunderstorm", "fa-bolt", "fa-bolt"));
        CODE_MAP.put(96, new WeatherMeta("Thunderstorm with Slight Hail", "fa-bolt-lightning", "fa-bolt-lightning"));
        CODE_MAP.put(99, new WeatherMeta("Thunderstorm with Heavy Hail", "fa-bolt-lightning", "fa-bolt-lightning"));
    }

    public static String getCondition(int code) {
        WeatherMeta meta = CODE_MAP.get(code);
        return meta != null ? meta.getCondition() : "Partly Cloudy";
    }

    public static String getIcon(int code, boolean isDay) {
        WeatherMeta meta = CODE_MAP.get(code);
        if (meta != null) {
            return isDay ? meta.getDayIcon() : meta.getNightIcon();
        }
        return isDay ? "fa-cloud-sun" : "fa-cloud-moon";
    }
}

