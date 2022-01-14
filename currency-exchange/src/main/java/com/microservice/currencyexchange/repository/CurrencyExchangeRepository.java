package com.microservice.currencyexchange.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.microservice.currencyexchange.entity.CurrencyExchange;

public interface CurrencyExchangeRepository extends JpaRepository<CurrencyExchange, Long> {
	
	CurrencyExchange findByFromAndTo(String from ,String to);

}
