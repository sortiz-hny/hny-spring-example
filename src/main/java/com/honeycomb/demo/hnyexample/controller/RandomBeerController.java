package com.honeycomb.demo.hnyexample.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.honeycomb.demo.hnyexample.model.Beer;
import com.honeycomb.demo.hnyexample.model.Quote;

import io.honeycomb.beeline.spring.beans.aspects.ChildSpan;
import io.honeycomb.beeline.tracing.Beeline;
import io.honeycomb.beeline.tracing.Span;
import io.honeycomb.beeline.tracing.SpanBuilderFactory;

@RestController
public class RandomBeerController {

	private final String BEER_ENDPOINT = "https://random-data-api.com/api/beer/random_beer";

	private final RestTemplate restTemplate;
	private final Beeline beeline;
	private final SpanBuilderFactory spanFactory;
	private ObjectMapper mapper;
	   
	@Autowired
	RandomBeerController(RestTemplate restTemplate, Beeline beeline) {
		this.restTemplate = restTemplate;
		this.beeline = beeline;
		this.spanFactory = beeline.getSpanBuilderFactory();
		
		mapper = new ObjectMapper();
	}

	@ChildSpan
    @GetMapping("/beer")
	public ResponseEntity<String> retrieveQuotes() throws JsonMappingException, JsonProcessingException {
		String response = "Howdy";
		try (Span childSpan = spanFactory.createBuilder()
			.setSpanName("random-beer")
			.setServiceName("hny-spring-boot-example")
			.addField("endpoint", BEER_ENDPOINT)
			.build()) {
			response = restTemplate
				.exchange(BEER_ENDPOINT, HttpMethod.GET, null, String.class)
				.getBody();
			
			Beer beer = mapper.readValue(response, Beer.class);
			if (beer != null) {
				childSpan.addField("beer.id", beer.getId());
				childSpan.addField("beer.name", beer.getName());
				childSpan.addField("beer.style", beer.getStyle());
			}
		} 
		return new ResponseEntity<String>(response, HttpStatus.OK);
	}
}
