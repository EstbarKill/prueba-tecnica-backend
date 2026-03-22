package com.company.prueba_tecnica.infrastructure.web.handler;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.company.prueba_tecnica.domain.exception.BusinessException;
import com.company.prueba_tecnica.infrastructure.web.response.ApiResponse;

import reactor.core.publisher.Mono;

/**
 * Manejador global de excepciones para toda la API.
 *
 * @RestControllerAdvice intercepta las excepciones no capturadas que se
 * propaguen desde cualquier controlador y las transforma en respuestas
 * HTTP estructuradas con el formato ApiResponse.
 *
 * Esto centraliza el manejo de errores y evita duplicar lógica de respuesta
 * en cada controlador o caso de uso.
 *
 * Mejoras pendientes:
 *   - Agregar @ExceptionHandler para NotFoundException → HTTP 404
 *   - Agregar @ExceptionHandler para DuplicateResourceException → HTTP 409
 *   - Agregar @ExceptionHandler para WebExchangeBindException → HTTP 422
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Maneja excepciones de tipo BusinessException (errores de lógica de negocio).
     *
     * Retorna HTTP 400 Bad Request con el mensaje y código semántico del error.
     * El campo "path" indica el método HTTP y ruta que generó la excepción.
     *
     * @param ex      la excepción de negocio capturada
     * @param request el request HTTP que originó el error (para extraer la ruta)
     * @return Mono con la respuesta HTTP 400 estructurada
     */
    @ExceptionHandler(BusinessException.class)
    public Mono<ResponseEntity<ApiResponse<Object>>> handleBusinessException(
            BusinessException ex,
            ServerHttpRequest request
    ) {
        // Construir la respuesta de error con el código semántico del dominio
        ApiResponse<Object> response = ApiResponse.error(
                ex.getMessage(),
                ex.getCode(),
                request.getMethod().name() + " " + request.getURI().getPath()
        );

        return Mono.just(ResponseEntity
                .badRequest()
                .body(response));
    }

    /**
     * Maneja cualquier excepción no contemplada por los otros handlers.
     *
     * Actúa como red de seguridad: retorna HTTP 500 con un mensaje genérico
     * para no exponer detalles internos del sistema al cliente.
     *
     * Nota: NotFoundException y DuplicateResourceException caen aquí por ahora,
     * retornando incorrectamente HTTP 500. Se recomienda agregar handlers
     * específicos para cada tipo de excepción del dominio.
     *
     * @param ex      la excepción no esperada capturada
     * @param request el request HTTP que originó el error
     * @return Mono con la respuesta HTTP 500 estructurada
     */
    @ExceptionHandler(Exception.class)
    public Mono<ResponseEntity<ApiResponse<Object>>> handleGenericException(
            Exception ex,
            ServerHttpRequest request
    ) {
        ApiResponse<Object> response = ApiResponse.error(
                "Internal server error",
                ex.getMessage(),
                request.getMethod().name() + " " + request.getURI().getPath()
        );

        return Mono.just(ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(response));
    }
}
