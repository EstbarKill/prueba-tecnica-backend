package com.company.prueba_tecnica.infrastructure.web.response;

import java.time.LocalDateTime;

/**
 * Envoltorio genérico para todas las respuestas de la API.
 *
 * Provee una estructura de respuesta uniforme para que los consumidores
 * de la API siempre reciban el mismo formato, tanto en éxito como en error.
 *
 * Ejemplo de respuesta exitosa:
 * {
 *   "success": true,
 *   "message": "OK",
 *   "data": { ... },
 *   "error": null,
 *   "path": "POST /franchises",
 *   "timestamp": "2025-03-22T14:30:00"
 * }
 *
 * Ejemplo de respuesta de error:
 * {
 *   "success": false,
 *   "message": "Franchise not found",
 *   "data": null,
 *   "error": "FRANCHISE_NOT_FOUND",
 *   "path": "GET /franchises/fr-999",
 *   "timestamp": "2025-03-22T14:31:00"
 * }
 *
 * @param <T> tipo del payload de respuesta en caso de éxito
 */
public class ApiResponse<T> {

    /** true si la operación fue exitosa, false si ocurrió un error. */
    private boolean success;

    /** Mensaje legible describiendo el resultado de la operación. */
    private String message;

    /** Payload de respuesta. null en caso de error. */
    private T data;

    /** Detalle del error. null en caso de éxito. */
    private Object error;

    /** Ruta HTTP que generó esta respuesta (ej: "POST /franchises"). */
    private String path;

    /** Momento exacto en que se generó la respuesta. */
    private LocalDateTime timestamp;

    /**
     * Constructor privado: se debe usar los métodos de fábrica success() o error().
     *
     * @param success indica si la operación fue exitosa
     * @param message descripción del resultado
     * @param data    payload de respuesta (null en errores)
     * @param error   detalle del error (null en éxito)
     * @param path    ruta HTTP que originó la respuesta
     */
    public ApiResponse(boolean success, String message, T data, Object error, String path) {
        this.success = success;
        this.message = message;
        this.data = data;
        this.error = error;
        this.path = path;
        // El timestamp se asigna automáticamente al momento de construcción
        this.timestamp = LocalDateTime.now();
    }

    /**
     * Método de fábrica para construir una respuesta exitosa.
     *
     * @param data    payload a retornar al cliente
     * @param message mensaje descriptivo del éxito
     * @param path    ruta HTTP que generó la respuesta
     * @param <T>     tipo del payload
     * @return ApiResponse con success=true y el payload
     */
    public static <T> ApiResponse<T> success(T data, String message, String path) {
        return new ApiResponse<>(true, message, data, null, path);
    }

    /**
     * Método de fábrica para construir una respuesta de error.
     *
     * @param message descripción del error
     * @param error   detalle técnico del error (código semántico u objeto)
     * @param path    ruta HTTP que generó la respuesta
     * @param <T>     tipo del payload (será null)
     * @return ApiResponse con success=false y el detalle del error
     */
    public static <T> ApiResponse<T> error(String message, Object error, String path) {
        return new ApiResponse<>(false, message, null, error, path);
    }
}
