package com.example.currencyaccount.service;

import com.example.currencyaccount.model.Account;
import com.example.currencyaccount.model.Currency;
import com.example.currencyaccount.repository.AccountRepository;
import com.example.currencyaccount.service.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;

    public Account getAccount(Long customerId, Currency currency) {
        return accountRepository.findByCustomerIdAndCurrency(customerId, currency)
                .orElseThrow((() -> new NotFoundException("Account not found")));
    }
}
