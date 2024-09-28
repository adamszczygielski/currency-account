package com.example.currencyaccount.dto;

import lombok.Builder;

import java.util.List;

@Builder
public record CustomerDto(Long id, String firstName, String lastName, List<AccountDto> accountDtos) {
}
