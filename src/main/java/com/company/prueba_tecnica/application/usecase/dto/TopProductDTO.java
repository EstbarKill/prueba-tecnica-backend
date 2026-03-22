package com.company.prueba_tecnica.application.usecase.dto;

/**
 * DTO que representa el producto con mayor stock dentro de una sucursal.
 *
 * Forma parte de la respuesta de GetTopProductByBranchInFranchiseUseCase,
 * anidado dentro de BranchWithProductsAndTopDTO.
 *
 * Puede ser null si la sucursal no tiene ningún producto registrado.
 */
public class TopProductDTO {

    /** Identificador del producto ganador. */
    private String productId;

    /** Nombre del producto ganador. */
    private String productName;

    /** Stock del producto ganador (el mayor de la sucursal). */
    private Integer stock;

    /**
     * Constructor completo requerido por GetTopProductByBranchInFranchiseUseCase.
     *
     * @param productId   identificador del producto
     * @param productName nombre del producto
     * @param stock       cantidad disponible (la mayor de la sucursal)
     */
    public TopProductDTO(String productId, String productName, Integer stock) {
        this.productId = productId;
        this.productName = productName;
        this.stock = stock;
    }

    public String getProductId() { return productId; }
    public String getProductName() { return productName; }
    public Integer getStock() { return stock; }
}