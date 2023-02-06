package com.example.Test;


public record WeatherData(String key, String city, double temperature, boolean correct, boolean CanCycle) {

    public WeatherData(String key, String city, double temperature, boolean correct, boolean CanCycle) {
        this.CanCycle = CanCycle;
        this.key = key;
        this.city = city;
        this.temperature = temperature;
        this.correct = correct;
    }

    public String getKey() {
        return key;
    }

    public String getCity() {
        return city;
    }

    public double getTemperature() {
        return temperature;
    }
}
