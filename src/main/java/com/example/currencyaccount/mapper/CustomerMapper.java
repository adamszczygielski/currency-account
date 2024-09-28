package com.example.currencyaccount.mapper;

import com.example.currencyaccount.dto.CustomerCreateDto;
import com.example.currencyaccount.dto.CustomerDto;
import com.example.currencyaccount.model.Account;
import com.example.currencyaccount.model.Customer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = AccountMapper.class)
public interface CustomerMapper {

    @Mapping(target = "accountDtos", source = "accounts")
    CustomerDto toCustomerDto(Customer customer);

    Customer toCustomer(CustomerCreateDto customerCreateDto, List<Account> accounts);
}
