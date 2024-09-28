package com.example.currencyaccount.dto;

import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record CustomerCreateDto(String firstName, String lastName, BigDecimal initialBalancePln) {}