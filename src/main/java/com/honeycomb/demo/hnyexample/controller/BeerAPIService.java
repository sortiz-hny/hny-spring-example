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
		if (beer != null) {
			childSpan.addField("beer.id", beer.getId());
			childSpan.addField("beer.name", beer.getName());
			childSpan.addField("beer.style", beer.getStyle());
		}
		return beer;
	}

}
