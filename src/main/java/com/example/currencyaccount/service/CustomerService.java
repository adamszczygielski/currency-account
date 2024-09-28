package com.example.currencyaccount.service;

import com.example.currencyaccount.dto.CustomerCreateDto;
import com.example.currencyaccount.dto.CustomerDto;
import com.example.currencyaccount.mapper.CustomerMapper;
import com.example.currencyaccount.model.Account;
import com.example.currencyaccount.model.Currency;
import com.example.currencyaccount.model.Customer;
import com.example.currencyaccount.repository.CustomerRepository;
import com.example.currencyaccount.service.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerMapper customerMapper;
    private final CustomerRepository customerRepository;

    public CustomerDto createCustomer(CustomerCreateDto customerCreateDto) {
        Account plnAccount = Account.builder()
                .currency(Currency.PLN)
                .balance(customerCreateDto.initialBalancePln())
                .build();

        Account usdAccount = Account.builder()
                .currency(Currency.USD)
                .balance(BigDecimal.ZERO)
                .build();

        Customer customer = customerMapper.toCustomer(customerCreateDto, List.of(plnAccount, usdAccount));
        return customerMapper.toCustomerDto(customerRepository.save(customer));
    }

    public CustomerDto getCustomer(long customerId) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new NotFoundException("Customer does not exist"));
        return customerMapper.toCustomerDto(customer);
    }
}
