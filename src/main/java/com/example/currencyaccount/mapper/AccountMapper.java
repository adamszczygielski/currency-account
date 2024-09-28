package com.example.currencyaccount.mapper;

import com.example.currencyaccount.dto.AccountDto;
import com.example.currencyaccount.model.Account;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AccountMapper {

    AccountDto toAccountDto(Account account);
    Account toAccount(AccountDto accountDto);
}
