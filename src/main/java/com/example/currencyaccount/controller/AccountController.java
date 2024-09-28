package com.example.currencyaccount.controller;

import com.example.currencyaccount.service.ExchangeMoneyService;
import com.example.currencyaccount.dto.ExchangeRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/accounts")
@Validated
@RequiredArgsConstructor
public class AccountController {

    private final ExchangeMoneyService exchangeMoneyService;

    @PostMapping("/exchange")
    public ResponseEntity<Void> exchange(@RequestBody ExchangeRequestDto exchangeRequestDto) {
        exchangeMoneyService.exchangeMoney(exchangeRequestDto);
        return ResponseEntity.ok().build();
    }
}
