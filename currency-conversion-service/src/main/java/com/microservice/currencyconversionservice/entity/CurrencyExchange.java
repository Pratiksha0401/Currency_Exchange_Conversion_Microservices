package com.microservice.currencyconversionservice.entity;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CurrencyExchange {
	
	private Long id;

	private String from;

	private String to;

	private BigDecimal conversionMultiple;

	private String environment;

}
