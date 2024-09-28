package com.example.currencyaccount.service;

import com.example.currencyaccount.dto.NbpResponseDto;
import com.example.currencyaccount.service.exception.InternalServerError;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ExchangeRateService {

    @Value("${nbp.url}")
    private String nbpUrl;

    private final RestTemplate restTemplate;

    public BigDecimal getExchangeRate() {
        ResponseEntity<NbpResponseDto> responseEntity;
        try {
             responseEntity = restTemplate.getForEntity(nbpUrl, NbpResponseDto.class);
        } catch (Exception e) {
            throw new InternalServerError("Service unavailable");
        }

        if (!responseEntity.getStatusCode().is2xxSuccessful()) {
            throw new InternalServerError("Service unavailable");
        }
        return Optional.ofNullable(responseEntity.getBody())
                .map(NbpResponseDto::getRates)
                .map(List::getFirst)
                .map(NbpResponseDto.Rate::getMid)
                .orElseThrow(()-> new InternalServerError("Bad response from NBP API"));
    }
}
