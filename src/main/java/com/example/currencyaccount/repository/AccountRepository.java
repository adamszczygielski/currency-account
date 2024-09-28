package com.example.currencyaccount.repository;

import com.example.currencyaccount.model.Account;
import com.example.currencyaccount.model.Currency;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {

    Optional<Account> findByCustomerIdAndCurrency(Long customerId, Currency currency);
}
