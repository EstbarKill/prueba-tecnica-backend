package com.company.prueba_tecnica.domain.exception;

/**
 * Excepción base para todos los errores de lógica de negocio.
 *
 * Extiende RuntimeException para no obligar a los llamadores
 * a declarar checked exceptions, manteniendo el código reactivo limpio.
 *
 * Incluye un campo 'code' con un identificador semántico del error
 * (ej: "BRANCH_NOT_FOUND") que se expone en la respuesta de la API
 * a través del GlobalExceptionHandler.
 */
public class BusinessException extends RuntimeException {

    /** Código semántico del error, usado en la respuesta JSON de la API. */
    private final String code;

    /**
     * Construye una excepción de negocio con mensaje y código.
     *
     * @param message descripción legible del error
     * @param code    identificador semántico (ej: "BRANCH_NOT_FOUND")
     */
    public BusinessException(String message, String code) {
        super(message);
        this.code = code;
    }

    /**
     * Retorna el código semántico del error.
     *
     * @return código del error
     */
    public String getCode() {
        return code;
    }
}
