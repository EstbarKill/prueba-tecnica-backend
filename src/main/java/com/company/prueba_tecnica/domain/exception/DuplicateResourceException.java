package com.company.prueba_tecnica.domain.exception;

/**
 * Excepción lanzada cuando se intenta crear un recurso que ya existe.
 *
 * Ejemplos de uso:
 *   - Franquicia con ID o nombre ya registrado
 *   - Sucursal con el mismo nombre dentro de una franquicia
 *   - Producto con el mismo nombre dentro de una sucursal
 *
 * El GlobalExceptionHandler debería mapear esta excepción a HTTP 409 Conflict.
 */
public class DuplicateResourceException extends RuntimeException {

    /**
     * Construye la excepción con una descripción del conflicto.
     *
     * @param message descripción del recurso duplicado
     */
    public DuplicateResourceException(String message) {
        super(message);
    }
}
