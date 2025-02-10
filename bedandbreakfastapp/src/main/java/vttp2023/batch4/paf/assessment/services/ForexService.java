package vttp2023.batch4.paf.assessment.services;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;

@Service
public class ForexService {

	// TODO: Task 5 
	public float convert(String from, String to, float amount) {

		StringBuilder urlBuilder = new StringBuilder("https://api.frankfurter.dev/v1/latest?base=");
		urlBuilder.append(from);
		urlBuilder.append("&symbols=");
		urlBuilder.append(to);
		String url = urlBuilder.toString();
		System.out.println(url);
		// String url = "https://api.frankfurter.dev/v1/latest?base=sgd";
		JsonObject jsonData = generateJson(url);
		double rate = jsonData.getJsonObject("rates").getJsonNumber(to.toUpperCase()).doubleValue();

		return (float) (amount*rate);
	}

	private JsonObject generateJson(String url){
	RestTemplate restTemplate = new RestTemplate();
	ResponseEntity<String> responseEntity = restTemplate.getForEntity(url, String.class);
	
	String respBody = responseEntity.getBody();
	InputStream is = new ByteArrayInputStream(respBody.getBytes());
	JsonReader jsonReader = Json.createReader(is);
	JsonObject jsonData = jsonReader.readObject();
	return jsonData;
    }
}
