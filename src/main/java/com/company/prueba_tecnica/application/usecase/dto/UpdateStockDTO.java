package com.company.prueba_tecnica.application.usecase.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * DTO de entrada para la operación de actualización de stock.
 *
 * Usado como cuerpo del request en:
 *   - PUT /products/{id}/stock
 *
 * Nota: @NotBlank está pensado para validar String, no Integer.
 * En un Integer siempre se ignora silenciosamente por Bean Validation.
 * Debería reemplazarse por @NotNull para validar correctamente
 * que el campo no llegue como null en el JSON.
 */
public class UpdateStockDTO {

        /**
     * Nuevo valor de stock para el producto.
     * Debe ser mayor o igual a cero (validado en Product.updateStock()).
     */
    @NotBlank(message = "Name cannot be empty")
    private Integer stock;
    
    /**
     * Constructor con el valor de stock.
     * Requerido por los tests unitarios que instancian este DTO directamente.
     *
     * @param stock nuevo valor de stock
     */
    public UpdateStockDTO(Integer stock) {
        this.stock = stock;
    }

    /**
     * Retorna el nuevo valor de stock enviado en el request.
     *
     * @return el nuevo stock
     */
    public Integer getStock() { return stock; }
}