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
import com.honeycomb.demo.hnyexample.model.Quote;

import io.honeycomb.beeline.spring.beans.aspects.ChildSpan;
import io.honeycomb.beeline.tracing.Beeline;
import io.honeycomb.beeline.tracing.Span;
import io.honeycomb.beeline.tracing.SpanBuilderFactory;

@RestController
public class QuotationsController {

	private final String QUOTATIONS_ENDPOINT = "https://gturnquist-quoters.cfapps.io/api/random";

	private final RestTemplate restTemplate;
	private final Beeline beeline;
	private final SpanBuilderFactory spanFactory;
	private ObjectMapper mapper;
	   
	@Autowired
	QuotationsController(RestTemplate restTemplate, Beeline beeline) {
		this.restTemplate = restTemplate;
		this.beeline = beeline;
		this.spanFactory = beeline.getSpanBuilderFactory();
		
		mapper = new ObjectMapper();
	}

	@ChildSpan
    @GetMapping("/quotes")
	public ResponseEntity<String> retrieveQuotes() throws JsonMappingException, JsonProcessingException {
		String response = "Howdy";
		try (Span childSpan = spanFactory.createBuilder()
			.setSpanName("random-quote")
			.setServiceName("hny-spring-boot-example")
			.addField("endpoint", QUOTATIONS_ENDPOINT)
			.build()) {
			response = restTemplate
				.exchange(QUOTATIONS_ENDPOINT, HttpMethod.GET, null, String.class)
				.getBody();
			
			Quote quote = mapper.readValue(response, Quote.class);
			if (quote.getValue() != null) {
				childSpan.addField("quote.id", quote.getValue().getId());
				childSpan.addField("quote.text", quote.getValue().getQuote());
			}
		} 
		return new ResponseEntity<String>(response, HttpStatus.OK);
	}
}
