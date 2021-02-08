package com.honeycomb.demo.hnyexample.config;

import java.net.URI;
import java.net.URISyntaxException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import io.honeycomb.beeline.DefaultBeeline;
import io.honeycomb.beeline.tracing.Beeline;
import io.honeycomb.libhoney.LibHoney;
import io.honeycomb.libhoney.TransportOptions;
import io.honeycomb.libhoney.shaded.org.apache.http.HttpHost;

@Configuration
public class DefaultConfig {
	
	private static final String PUBLIC_API = "https://api.honeycomb.io";
	private static final String LOCAL_BASENJI = "http://localhost:8888";
	private static final String LOCAL_SHEPHERD = "http://localhost:8081";
	
	@Value("${honeycomb.beeline.service-name}")
	String serviceName;
	
	@Value("${honeycomb.beeline.dataset}")
	String datasetName;
	
	@Value("${honeycomb.beeline.write-key}")
	String writeKey;
	
	//For local basenji, use: http://localhost:8888
	//For local shepherd, use: http://localhost:8081
	@Value("${honeycomb.beeline.api-host:" + PUBLIC_API + "}")
	String honeycombHost;
	
	
    @Bean
	public RestTemplate restTemplate(RestTemplateBuilder builder) {
		return builder.build();
	}
    
    @Bean
    public Beeline configureBeeline() throws URISyntaxException {
    	System.out.println("[sortiz] **************************** " + honeycombHost);
    	
    	if (PUBLIC_API.equals(honeycombHost)) {
    		return DefaultBeeline.getInstance(datasetName, serviceName, writeKey).getBeeline();
    		
    	} else {
    		//Must be a local shephered or basenji
    		
    		//Only use transport options IFF we're using basenji
    		TransportOptions transportOptions = null;
    		if (LOCAL_BASENJI.equals(honeycombHost)) {
            	String[] tokens = honeycombHost.split(":");
            	String protocol = tokens[0];
            	String host = tokens[1];
            	String port = tokens[2];
            	
            	//Setup the basenji proxy
            	transportOptions = LibHoney
            			.transportOptions()
            			.setProxy(new HttpHost(host, Integer.parseInt(port), protocol))
            			.build();
    		}
        	
        	DefaultBeeline beeline = DefaultBeeline
        			.getInstance(datasetName, 
        					serviceName, 
        					writeKey, 
        					new URI(honeycombHost),
        					transportOptions);
        	return beeline.getBeeline();
    	}
    }
    
}
