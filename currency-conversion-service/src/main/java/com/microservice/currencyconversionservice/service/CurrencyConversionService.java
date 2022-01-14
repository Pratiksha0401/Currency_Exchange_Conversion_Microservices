package com.microservice.currencyconversionservice.service;

import java.math.BigDecimal;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.microservice.currencyconversionservice.entity.CurrencyConversion;
import com.microservice.currencyconversionservice.entity.CurrencyExchange;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;

@Service
public class CurrencyConversionService {
	
	@Autowired
	RestTemplate restTemplate;
	
	private static final String CURRENCY_EXCHANGE = "currency-exchange";
	
	@CircuitBreaker(name=CURRENCY_EXCHANGE, fallbackMethod = "currencyExchangeFallback")
	public CurrencyConversion calculateCurrencyConversion(String from, String to, BigDecimal quantity) {
		HashMap<String, String> uriVariables = new HashMap<>();
		uriVariables.put("from",from);
		uriVariables.put("to",to);
		
		ResponseEntity<CurrencyExchange> responseEntity = this.restTemplate.getForEntity
				("http://currency-exchange/currency-exchange/from/{from}/to/{to}", 
						CurrencyExchange.class, uriVariables);
//		("http://localhost:8000/currency-exchange/from/{from}/to/{to}", 
//				CurrencyConversion.class, from,to);
		
		CurrencyExchange currencyExchange = responseEntity.getBody();
		System.out.println("body"+currencyExchange);
		return new CurrencyConversion(currencyExchange.getId(), 
				from, to, quantity, 
				currencyExchange.getConversionMultiple(), 
				quantity.multiply(currencyExchange.getConversionMultiple()), 
				currencyExchange.getEnvironment()+ " " + "rest template");
	}
	
	public CurrencyConversion currencyExchangeFallback(String from, String to, BigDecimal quantity,Exception e){
		System.out.println("Fallback");
		CurrencyConversion conversion=null;
		return conversion;
	}
	

}
