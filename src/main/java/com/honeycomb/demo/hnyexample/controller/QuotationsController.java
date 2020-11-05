package com.honeycomb.demo.hnyexample.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import io.honeycomb.beeline.tracing.Beeline;
import io.honeycomb.beeline.tracing.Span;
import io.honeycomb.beeline.tracing.SpanBuilderFactory;

@RestController
public class QuotationsController {

	private final String QUOTATIONS_ENDPOINT = "https://gturnquist-quoters.cfapps.io/api/random";

	private final RestTemplate restTemplate;
	private final Beeline beeline;
	private final SpanBuilderFactory spanFactory;
	   
	@Autowired
	QuotationsController(RestTemplate restTemplate, Beeline beeline) {
		this.restTemplate = restTemplate;
		this.beeline = beeline;
		this.spanFactory = beeline.getSpanBuilderFactory();
	}

    @GetMapping("/quotes")
	public ResponseEntity<String> retrieveQuotes() {
		String response = "Howdy";
		try (Span childSpan = spanFactory.createBuilder()
			.setSpanName("async-request")
			.setServiceName("hny-spring-boot-example")
			.build()) {
			childSpan.addField("endpoint", QUOTATIONS_ENDPOINT);
			response = restTemplate
				.exchange(QUOTATIONS_ENDPOINT, HttpMethod.GET, null, String.class)
				.getBody();
		} 
		return new ResponseEntity<String>(response, HttpStatus.OK);
	}
}
