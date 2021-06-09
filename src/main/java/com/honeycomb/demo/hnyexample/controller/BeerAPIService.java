package com.honeycomb.demo.hnyexample.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.honeycomb.demo.hnyexample.model.Beer;

import io.opentelemetry.api.trace.Span;

@Service
public class BeerAPIService {
	
	private ObjectMapper mapper;
	private final RestTemplate restTemplate;
	
	private static final String BEER_ENDPOINT = "https://random-data-api.com/api/beer/random_beer";
	
	@Autowired
	public BeerAPIService(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
		mapper = new ObjectMapper();
	}
	
	public Beer getBeer() throws JsonMappingException, JsonProcessingException {
		Span span = Span.current();
		String response = restTemplate
				.exchange(BEER_ENDPOINT, HttpMethod.GET, null, String.class)
				.getBody();
		
		Beer beer = mapper.readValue(response, Beer.class);
		if (beer != null) {
			String percentStr = beer.getAlcohol().substring(0, beer.getAlcohol().length()-1);
			double percent = Double.parseDouble(percentStr);
			
			String ibuStr = beer.getIbu().split(" ")[0];
			int ibu = Integer.parseInt(ibuStr);
			
			span.setAttribute("beer.id", beer.getId());
			span.setAttribute("beer.name", beer.getName());
			span.setAttribute("beer.style", beer.getStyle());
			span.setAttribute("beer.brand", beer.getBrand());
			span.setAttribute("beer.ibu", ibu);
			span.setAttribute("beer.alcohol_percent", percent);
			span.setAttribute("beer.misc", beer.getHop() + "\n " + beer.getMalts() + " \n" + beer.getYeast());
		}
		
		return beer;
	}

}
