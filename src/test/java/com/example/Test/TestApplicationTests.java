package com.example.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestTemplate;
import static org.junit.jupiter.api.Assertions.assertEquals;

@RunWith(MockitoJUnitRunner.class)
class TestApplicationTests {

	@InjectMocks
	WeatherController weatherController;

	@Mock
	RestTemplate restTemplate;

	@Test
	void MockTestGetWeather() throws JsonProcessingException {
		String locationJsonString = "[{\"Key\":\"12345\",\"LocalizedName\":\"Amsterdam\"}]";
		String temperatureJsonString = "[{\"Temperature\":{\"Metric\":{\"Value\":20.0}}}]";

		JsonNode locationData = new ObjectMapper().readTree(locationJsonString);
		JsonNode temperatureData = new ObjectMapper().readTree(temperatureJsonString);

		Mockito.when(restTemplate.getForObject("http://dataservice.accuweather.com/locations/v1/cities/search?apikey=NgmACfJgwsiV6ZVspgnZGDwA0bFNPq5a&q=Amsterdam&language=nl-nl", JsonNode.class))
				.thenReturn(locationData);
		Mockito.when(restTemplate.getForObject("http://dataservice.accuweather.com/currentconditions/v1/12345?apikey=NgmACfJgwsiV6ZVspgnZGDwA0bFNPq5a", JsonNode.class))
				.thenReturn(temperatureData);

		Object result = weatherController.getWeather("Amsterdam");

		WeatherData expected = new WeatherData("12345", "Amsterdam", 20.0, true, true);
		assertEquals(expected, result);
	}

	@Test
	void TestWeatherDataReturnsValues() {
		WeatherData weatherData = new WeatherData("249758", "Amsterdam", 6.1, true, true);
		assertEquals("249758", weatherData.getKey());
		assertEquals("Amsterdam", weatherData.getCity());
		assertEquals(6.1, weatherData.getTemperature());
		assertEquals(true, weatherData.correct());
		assertEquals(true, weatherData.CanCycle());
	}

	@Test
	void TestWrongValueReturnsCorrectError()
	{
		//Test eerst city = null;
		String nullCity = " ";
		Object response = weatherController.getWeather(nullCity);
		assertEquals("Geen stad ingevoerd", response);

		//Test city = niet bestaande stad
		String wrongCity = "Bestaat niet";
		Object response1 =  weatherController.getWeather(wrongCity);
		assertEquals("Ongeldige stad of bestaat niet", response1);

	}
}
