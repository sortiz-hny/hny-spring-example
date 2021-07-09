package com.honeycomb.demo.hnyexample.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.honeycomb.demo.hnyexample.model.Beer;

import io.honeycomb.beeline.spring.beans.aspects.ChildSpan;
import io.honeycomb.beeline.tracing.Beeline;
import io.honeycomb.beeline.tracing.Span;

@Service
public class BeerAPIService {
	
	private ObjectMapper mapper;
	private final RestTemplate restTemplate;
	private final Beeline beeline;
	
	private static final String BEER_ENDPOINT = "https://random-data-api.com/api/beer/random_beer";
	
	@Autowired
	public BeerAPIService(RestTemplate restTemplate, Beeline beeline) {
		this.restTemplate = restTemplate;
		mapper = new ObjectMapper();
		this.beeline = beeline;
	}
	
	@ChildSpan("getBeer")
	public Beer getBeer() throws JsonMappingException, JsonProcessingException {
		Span childSpan = beeline.getActiveSpan();
		childSpan.addField("external-rest-endpoint", BEER_ENDPOINT);
		
		String response = restTemplate
				.exchange(BEER_ENDPOINT, HttpMethod.GET, null, String.class)
				.getBody();
		
		Beer beer = mapper.readValue(response, Beer.class);
		populateSpanFromBeer(beer, childSpan);
		return beer;
	}
	
	public static void populateSpanFromBeer(Beer beer, Span span) {
		if (beer != null) {
			String percentStr = beer.getAlcohol().substring(0, beer.getAlcohol().length()-1);
			double percent = Double.parseDouble(percentStr);
			
			String ibuStr = beer.getIbu().split(" ")[0];
			int ibu = Integer.parseInt(ibuStr);
			
			span.addField("beer.id", beer.getId());
			span.addField("beer.name", beer.getName());
			span.addField("beer.style", beer.getStyle());
			span.addField("beer.brand", beer.getBrand());
			span.addField("beer.ibu", ibu);
			span.addField("beer.alcohol_percent", percent);
			span.addField("beer.misc", beer.getHop() + "\n " + beer.getMalts() + " \n" + beer.getYeast());
			span.addField("beer.uuid", beer.getUuid());
		}
	}

}
