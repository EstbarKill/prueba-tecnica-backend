package com.company.prueba_tecnica.domain.exception;

/**
 * Excepción lanzada cuando un recurso solicitado no existe en el sistema.
 *
 * Ejemplos de uso:
 *   - Sucursal no encontrada por ID
 *   - Producto no encontrado por ID
 *   - Franquicia no encontrada al consultar top-products
 *
 * El GlobalExceptionHandler debería mapear esta excepción a HTTP 404 Not Found.
 */
public class NotFoundException extends RuntimeException {

    /**
     * Construye la excepción con una descripción del recurso no encontrado.
     *
     * @param message descripción del recurso que no fue hallado
     */
    public NotFoundException(String message) {
        super(message);
    }
}
