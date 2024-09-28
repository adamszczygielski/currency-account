package com.example.currencyaccount.service;

import com.example.currencyaccount.dto.ExchangeRequestDto;
import com.example.currencyaccount.model.Account;
import com.example.currencyaccount.model.Currency;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
@RequiredArgsConstructor
public class ExchangeMoneyService {

    private final ExchangeRateService exchangeRateService;
    private final AccountService accountService;

    @Transactional
    public void exchangeMoney(ExchangeRequestDto exchangeRequestDto) {
        BigDecimal exchangeRate = exchangeRateService.getExchangeRate();

        if (exchangeRequestDto.targetCurrency().equals(Currency.USD)) {
            BigDecimal requiredAmount = exchangeRequestDto.targetAmount().multiply(exchangeRate);

            transfer(exchangeRequestDto.customerId(), requiredAmount, Currency.PLN,
                    exchangeRequestDto.targetAmount(), Currency.USD);
        } else {
            BigDecimal requiredAmount = exchangeRequestDto.targetAmount().divide(exchangeRate, RoundingMode.DOWN);

            transfer(exchangeRequestDto.customerId(), requiredAmount, Currency.USD,
                    exchangeRequestDto.targetAmount(), Currency.PLN);
        }
    }

    public void transfer(Long customerId, BigDecimal sourceAmount, Currency sourceCurrency,
                         BigDecimal targetAmount, Currency targetCurrency) {

        Account sourceAccount = accountService.getAccount(customerId, sourceCurrency);
        sourceAccount.withdraw(sourceAmount);

        Account targetAccount = accountService.getAccount(customerId, targetCurrency);
        targetAccount.deposit(targetAmount);
    }
}
