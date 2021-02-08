package com.honeycomb.demo.hnyexample.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.honeycomb.demo.hnyexample.model.Beer;

import io.honeycomb.beeline.tracing.Beeline;
import io.honeycomb.beeline.tracing.Span;

@RestController
public class RandomBeerController {
	private final Beeline beeline;
	private BeerAPIService beerService;
	
	   
	@Autowired
	RandomBeerController(Beeline beeline, BeerAPIService beerService) {
		this.beeline = beeline;
		this.beerService = beerService;
	}

	@GetMapping(path = "/beer", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Beer> untapBeer() throws JsonMappingException, JsonProcessingException {
		Beer beer = null;
		try (Span childSpan = beeline.getSpanBuilderFactory().createBuilder()
			.setSpanName("untap-beer")
			.setServiceName("hny-spring-boot-example")
			.build()) {
			beer = beerService.getBeer();
		} 
		
		return new ResponseEntity<Beer>(beer, HttpStatus.OK);
	}
}
