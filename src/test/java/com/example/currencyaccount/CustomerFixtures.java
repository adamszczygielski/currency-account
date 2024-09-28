package com.example.currencyaccount;

import com.example.currencyaccount.dto.CustomerCreateDto;
import com.example.currencyaccount.model.Account;
import com.example.currencyaccount.model.Currency;
import com.example.currencyaccount.model.Customer;

import java.math.BigDecimal;
import java.util.List;

public class CustomerFixtures {

    public static CustomerCreateDto customerCreateDto() {
        return CustomerCreateDto.builder()
                .firstName("John")
                .lastName("Smith")
                .initialBalancePln(new BigDecimal("1000.00"))
                .build();
    }

    public static Customer customer(BigDecimal pln, BigDecimal usd) {
        Account plnAccount = Account.builder()
                .balance(pln)
                .currency(Currency.PLN)
                .build();

        Account usdAccount = Account.builder()
                .balance(usd)
                .currency(Currency.USD)
                .build();

        return Customer.builder()
                .firstName("John")
                .lastName("Smith")
                .accounts(List.of(plnAccount, usdAccount))
                .build();
    }
}
