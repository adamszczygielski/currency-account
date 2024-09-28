package com.example.currencyaccount;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

public class RestUtils {

    private static final RestTemplate REST_TEMPLATE = new RestTemplate();

    public static <T> ResponseEntity<T> consumeRestEndpoint(HttpMethod httpMethod, String url, HttpEntity<?> requestEntity,
                                                            Class<T> responseType) {
        try {
            return REST_TEMPLATE.exchange(url, httpMethod, requestEntity, responseType);
        } catch (RestClientResponseException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAs(responseType));
        }
    }

    public static <T> HttpEntity<T> toHttpEntity(T body) {
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.setContentType(MediaType.APPLICATION_JSON);
        return new HttpEntity<>(body, requestHeaders);
    }
}