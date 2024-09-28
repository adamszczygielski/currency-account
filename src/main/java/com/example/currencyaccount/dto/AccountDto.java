package com.example.currencyaccount.dto;

import com.example.currencyaccount.model.Currency;
import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record AccountDto(Long id, Currency currency, BigDecimal balance) {}
