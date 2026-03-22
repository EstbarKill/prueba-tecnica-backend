package com.company.prueba_tecnica.application.usecase.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * DTO de entrada para operaciones de actualización de nombre.
 *
 * Usado como cuerpo del request en:
 *   - PUT /franchises/{id}/name
 *   - PUT /branches/{id}/name
 *   - PUT /products/{id}/name
 *
 * La anotación @NotBlank garantiza que el campo no sea null,
 * vacío ni contenga solo espacios en blanco.
 * Spring Validation rechaza la petición con HTTP 400 si no se cumple.
 */
public class UpdateNameDTO {

    /**
     * Nuevo nombre a asignar al recurso.
     * No puede ser nulo, vacío ni solo espacios en blanco.
     */
    @NotBlank(message = "Name cannot be empty")
    private String name;

    /** Constructor vacío requerido por Jackson para deserializar el JSON del body. */
    public UpdateNameDTO() {}

    /**
     * Retorna el nuevo nombre enviado en el request.
     *
     * @return el nuevo nombre
     */
    public String getName() { return name; }
}