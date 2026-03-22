package com.company.prueba_tecnica.application.usecase.dto;

/**
 * DTO de respuesta para un Producto.
 *
 * Representa los datos públicos de un producto en las respuestas de la API.
 * Se usa como parte anidada en BranchDTO y como respuesta directa en:
 *   - UpdateProductNameUseCase
 *   - UpdateProductStockUseCase
 */
public class ProductDTO {

    /** Identificador único del producto. */
    private String id;

    /** Nombre del producto. */
    private String name;

    /** Cantidad disponible en inventario. */
    private Integer stock;

    /**
     * Constructor completo requerido por los use cases.
     *
     * @param id    identificador del producto
     * @param name  nombre del producto
     * @param stock cantidad disponible en inventario
     */
    public ProductDTO(String id, String name, Integer stock) {
        this.id = id;
        this.name = name;
        this.stock = stock;
        
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public Integer getStock() { return stock; }
}