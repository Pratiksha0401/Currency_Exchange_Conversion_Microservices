package com.microservice.currencyconversionservice.controller;

import java.math.BigDecimal;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.microservice.currencyconversionservice.CurrencyExchangeProxy;
import com.microservice.currencyconversionservice.entity.CurrencyConversion;
import com.microservice.currencyconversionservice.service.CurrencyConversionService;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;

@RestController
public class CurrencyConversionController {

	@Autowired
	private CurrencyExchangeProxy proxy;
	
	@Autowired
	CurrencyConversionService conversionService;
	
	private static final String CURRENCY_EXCHANGE = "currency-exchange";
	
	@GetMapping("/currency-conversion/from/{from}/to/{to}/quantity/{quantity}")
	public CurrencyConversion calculateCurrencyConversion(
			@PathVariable String from,
			@PathVariable String to,
			@PathVariable BigDecimal quantity
			) {
		return conversionService.calculateCurrencyConversion(from, to, quantity);
		
	}
	
	@GetMapping("/currency-conversion-feign/from/{from}/to/{to}/quantity/{quantity}")
	@CircuitBreaker(name=CURRENCY_EXCHANGE, fallbackMethod = "currencyExchangeFallback")
	public CurrencyConversion calculateCurrencyConversionFeign(
			@PathVariable String from,
			@PathVariable String to,
			@PathVariable BigDecimal quantity
			) {
				
		CurrencyConversion currencyConversion = proxy.retrieveExchangeValue(from, to);
		
		return new CurrencyConversion(currencyConversion.getId(), 
				from, to, quantity, 
				currencyConversion.getConversionMultiple(), 
				quantity.multiply(currencyConversion.getConversionMultiple()), 
				currencyConversion.getEnvironment() + " " + "feign");
		
	}
	
	public CurrencyConversion currencyExchangeFallback(String from, String to, BigDecimal quantity,Exception e){
		System.out.println("Fallback");
		CurrencyConversion conversion=null;
		return conversion;
	}
	
}
