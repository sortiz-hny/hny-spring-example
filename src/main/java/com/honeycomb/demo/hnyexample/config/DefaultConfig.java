package com.honeycomb.demo.hnyexample.config;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class DefaultConfig {
	
	static final String PUBLIC_API = "https://api.honeycomb.io";
	static final String LOCAL_BASENJI = "http://localhost:8888";
	static final String LOCAL_SHEPHERD = "http://localhost:8081";
	
    @Bean
	public RestTemplate restTemplate(RestTemplateBuilder builder) {
		return builder.build();
	}
    
}
