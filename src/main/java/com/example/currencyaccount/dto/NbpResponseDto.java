package com.example.currencyaccount.dto;

import lombok.Getter;

import java.math.BigDecimal;
import java.util.List;

@Getter
public class NbpResponseDto {

    private List<Rate> rates;

    @Getter
    public static class Rate {
        private BigDecimal mid;
    }
}
