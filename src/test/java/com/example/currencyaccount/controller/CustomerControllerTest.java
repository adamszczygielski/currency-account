package com.example.currencyaccount.controller;

import com.example.currencyaccount.RestUtils;
import com.example.currencyaccount.dto.AccountDto;
import com.example.currencyaccount.dto.CustomerCreateDto;
import com.example.currencyaccount.dto.CustomerDto;
import com.example.currencyaccount.model.Currency;
import com.example.currencyaccount.repository.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CustomerControllerTest {

    @Autowired
    CustomerRepository customerRepository;

    @Value("${local.server.port}")
    int port;

    @BeforeEach
    void purge() {
        customerRepository.deleteAll();
    }

    @Test
    void shouldCreateCustomer() {
        // given
        String url = "http://localhost:" + port + "/customers";
        CustomerCreateDto customerCreateDto = CustomerCreateDto.builder()
                .firstName("John")
                .lastName("Smith")
                .initialBalancePln(new BigDecimal("1000.00"))
                .build();

        // when
        ResponseEntity<CustomerDto> responseEntity = RestUtils.consumeRestEndpoint(HttpMethod.POST, url,
                RestUtils.toHttpEntity(customerCreateDto), CustomerDto.class);

        // then
        assertTrue(responseEntity.getStatusCode().is2xxSuccessful());

        CustomerDto body = responseEntity.getBody();
        assertNotNull(body);

        assertEquals("John", body.firstName());
        assertEquals("Smith", body.lastName());

        List<AccountDto> accountDtos = body.accountDtos();
        assertNotNull(accountDtos);
        assertEquals(2, accountDtos.size());

        Optional<AccountDto> plnAccount = accountDtos.stream()
                .filter(a -> a.currency().equals(Currency.PLN))
                .findFirst();
        assertTrue(plnAccount.isPresent());
        assertEquals("1000.00", plnAccount.get().balance().toString());

        Optional<AccountDto> usdAccount = accountDtos.stream()
                .filter(a -> a.currency().equals(Currency.USD))
                .findFirst();
        assertTrue(usdAccount.isPresent());
        assertEquals("0", usdAccount.get().balance().toString());
    }
}
