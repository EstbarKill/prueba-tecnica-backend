package com.company.prueba_tecnica.infrastructure.web.handler;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.company.prueba_tecnica.domain.exception.BusinessException;
import com.company.prueba_tecnica.infrastructure.web.response.ApiResponse;

import reactor.core.publisher.Mono;

@RestControllerAdvice   // 🔥 IMPORTANTE
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public Mono<ResponseEntity<ApiResponse<Object>>> handleBusinessException(
            BusinessException ex,
            ServerHttpRequest request
    ) {

        ApiResponse<Object> response = ApiResponse.error(
                ex.getMessage(),
                ex.getCode(),
                request.getMethod() + " " + request.getURI().getPath()
        );

        return Mono.just(ResponseEntity
                .badRequest()
                .body(response));
    }

    @ExceptionHandler(Exception.class)
    public Mono<ResponseEntity<ApiResponse<Object>>> handleGenericException(
            Exception ex,
            ServerHttpRequest request
    ) {

        ApiResponse<Object> response = ApiResponse.error(
                "Internal server error",
                ex.getMessage(),
                request.getMethod() + " " + request.getURI().getPath()
        );

        return Mono.just(ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(response));
    }
}