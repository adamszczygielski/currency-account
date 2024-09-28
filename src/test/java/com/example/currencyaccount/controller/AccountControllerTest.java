package com.example.currencyaccount.controller;

import com.example.currencyaccount.CustomerFixtures;
import com.example.currencyaccount.RestUtils;
import com.example.currencyaccount.dto.ErrorDto;
import com.example.currencyaccount.service.ExchangeRateService;
import com.example.currencyaccount.dto.ExchangeRequestDto;
import com.example.currencyaccount.model.Account;
import com.example.currencyaccount.model.Currency;
import com.example.currencyaccount.model.Customer;
import com.example.currencyaccount.repository.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AccountControllerTest {

    @Autowired
    CustomerRepository customerRepository;

    @Mock
    ExchangeRateService exchangeRateService;

    @Value("${local.server.port}")
    int port;

    @BeforeEach
    void purge() {
        customerRepository.deleteAll();
    }

    @Test
    void shouldThrowException() {
        // given
        String url = "http://localhost:" + port + "/accounts/exchange";
        long customerId = createCustomer(new BigDecimal("5.00"), BigDecimal.ZERO);
        ExchangeRequestDto exchangeRequestDto = ExchangeRequestDto.builder()
                .customerId(customerId)
                .targetAmount(new BigDecimal("10.00"))
                .targetCurrency(Currency.USD)
                .build();
        when(exchangeRateService.getExchangeRate()).thenReturn(new BigDecimal("3.8368"));

        //when
        ResponseEntity<ErrorDto> responseEntity = RestUtils.consumeRestEndpoint(HttpMethod.POST, url,
                RestUtils.toHttpEntity(exchangeRequestDto), ErrorDto.class);

        //then
        assertTrue(responseEntity.getStatusCode().is4xxClientError());
        assertNotNull(responseEntity.getBody());
        assertEquals("Insufficient amount of money on PLN account", responseEntity.getBody().message());

    }

    @Test
    void shouldExchangeFromPLNtoUSD() {
        // given
        String url = "http://localhost:" + port + "/accounts/exchange";
        long customerId = createCustomer(new BigDecimal("1000.00"), BigDecimal.ZERO);
        ExchangeRequestDto exchangeRequestDto = ExchangeRequestDto.builder()
                .customerId(customerId)
                .targetAmount(new BigDecimal("100.00"))
                .targetCurrency(Currency.USD)
                .build();

        when(exchangeRateService.getExchangeRate()).thenReturn(new BigDecimal(("3.8368")));

        // when
        ResponseEntity<ErrorDto> responseEntity = RestUtils.consumeRestEndpoint(HttpMethod.POST, url,
                RestUtils.toHttpEntity(exchangeRequestDto), ErrorDto.class);

        // then
        assertTrue(responseEntity.getStatusCode().is2xxSuccessful());

        Customer customer = customerRepository.findById(customerId).orElseThrow();

        BigDecimal plnBalance = customer.getAccounts().stream()
                .filter(a -> a.getCurrency().equals(Currency.PLN))
                .findFirst()
                .map(Account::getBalance).orElseThrow();

        assertEquals(new BigDecimal(("616.32")), plnBalance);

        BigDecimal usdBalance = customer.getAccounts().stream()
                .filter(a -> a.getCurrency().equals(Currency.USD))
                .findFirst()
                .map(Account::getBalance).orElseThrow();

        assertEquals(new BigDecimal("100.00"), usdBalance);
    }

    @Test
    void shouldExchangeFromUSDtoPLN() {
        // given
        String url = "http://localhost:" + port + "/accounts/exchange";
        long customerId = createCustomer(BigDecimal.ZERO, new BigDecimal("1000.00"));
        ExchangeRequestDto exchangeRequestDto = ExchangeRequestDto.builder()
                .customerId(customerId)
                .targetAmount(new BigDecimal("100.00"))
                .targetCurrency(Currency.PLN)
                .build();
        when(exchangeRateService.getExchangeRate()).thenReturn(new BigDecimal("3.8368"));

        // when
        ResponseEntity<ErrorDto> responseEntity = RestUtils.consumeRestEndpoint(HttpMethod.POST, url,
                RestUtils.toHttpEntity(exchangeRequestDto), ErrorDto.class);

        // then
        assertTrue(responseEntity.getStatusCode().is2xxSuccessful());

        Customer customer = customerRepository.findById(customerId).orElseThrow();

        BigDecimal plnBalance = customer.getAccounts().stream()
                .filter(a -> a.getCurrency().equals(Currency.PLN))
                .findFirst()
                .map(Account::getBalance).orElseThrow();
        assertEquals(new BigDecimal("100.00"), plnBalance);

        BigDecimal usdBalance = customer.getAccounts().stream()
                .filter(a -> a.getCurrency().equals(Currency.USD))
                .findFirst()
                .map(Account::getBalance).orElseThrow();

        assertEquals(new BigDecimal("973.94"), usdBalance);
    }

    long createCustomer(BigDecimal pln, BigDecimal usd) {
        Customer customer = CustomerFixtures.customer(pln, usd);
        return customerRepository.save(customer).getId();
    }
}
