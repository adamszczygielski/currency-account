package com.example.currencyaccount.dto;

import com.example.currencyaccount.model.Currency;
import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record ExchangeRequestDto(Long customerId, Currency targetCurrency, BigDecimal targetAmount) {}
