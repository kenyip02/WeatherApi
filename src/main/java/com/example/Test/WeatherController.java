package com.example.Test;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
    public class WeatherController {
    public String LOCATION_URL = "http://dataservice.accuweather.com/locations/v1/cities/search?apikey=NgmACfJgwsiV6ZVspgnZGDwA0bFNPq5a&q={city}&language=nl-nl";
    public String TEMPERATURE_URL = "http://dataservice.accuweather.com/currentconditions/v1/{locationKey}?apikey=NgmACfJgwsiV6ZVspgnZGDwA0bFNPq5a";

    @GetMapping("/weather")
    public Object getWeather(@RequestParam(value = "city") String city) {
        if (city.isEmpty()) {
            return "Geen stad ingevoerd";
        }

        RestTemplate restTemplate = new RestTemplate();
        String locationURL = LOCATION_URL.replace("{city}", city);
        JsonNode locationData = restTemplate.getForObject(locationURL, JsonNode.class);

        if (locationData.isArray() && locationData.size() > 0) {
            locationData = locationData.get(0);
            String location = locationData.get("Key").asText();

            String temperatureURL = TEMPERATURE_URL.replace("{locationKey}", location);
            JsonNode temperatureData = restTemplate.getForObject(temperatureURL, JsonNode.class).get(0);
            double temperature = temperatureData.get("Temperature").get("Metric").get("Value").asDouble();

            return new WeatherData(location, city, temperature, true, temperature > 10);
        } else {
            return "Ongeldige stad of bestaat niet";
        }

    }
}
