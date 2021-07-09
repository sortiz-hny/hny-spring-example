package com.honeycomb.demo.hnyexample.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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
import io.honeycomb.beeline.tracing.SpanBuilderFactory;

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
	
	@GetMapping(path = "/beers-identical", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<Beer>> generateIdenticalBeers() throws JsonMappingException, JsonProcessingException {
		List<Beer> beers = new ArrayList<Beer>();
		
		for (int i=0; i<1250; i++) {			
			try (Span span = beeline.getSpanBuilderFactory().createBuilder()
				.setSpanName("untap-beer")
				.setServiceName("hny-spring-boot-example")
				.build()) {
				
				//Generate a new beer
				Beer beer = new Beer();
				beer.setId(System.nanoTime());
				beer.setBrand("Becks");
				beer.setName("HopSlam Ale");
				beer.setStyle("Light Lager");
				beer.setHop("Amarillo-");
				beer.setYeast("1214 - Belgian Abbey" );
				beer.setMalts("Roasted barley");
				beer.setIbu("44");
				beer.setAlcohol("4.6");
				beer.setBlg("14.1°Blg");
				beer.setUuid(UUID.randomUUID().toString());
				beers.add(beer);
				
				BeerAPIService.populateSpanFromBeer(beer, span);
				
				if (i % 10 == 0) {
					try {
						Thread.sleep(10);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			} 
		}
		
		return new ResponseEntity<List<Beer>>(beers, HttpStatus.OK);
	}
}
