package com.company.prueba_tecnica.application.usecase.dto;

import java.util.List;

/**
 * DTO de respuesta para una sucursal dentro del endpoint top-products.
 *
 * Combina en un solo objeto:
 *   - Los datos básicos de la sucursal (id y nombre)
 *   - El producto con mayor stock (topProduct)
 *   - La lista completa de todos los productos de la sucursal
 *
 * Se usa como elemento de la lista en FranchiseTopProductsDTO.
 */
public class BranchWithProductsAndTopDTO {

    /** Identificador único de la sucursal. */
    private String branchId;
    
     /** Nombre de la sucursal. */
    private String branchName;

    /**
     * El producto con mayor stock en esta sucursal.
     * Será null si la sucursal no tiene productos.
     */
    private TopProductDTO topProduct;

    /** Lista completa de todos los productos de la sucursal. */
    private List<ProductDTO> products;

    /**
     * Constructor completo requerido por GetTopProductByBranchInFranchiseUseCase.
     *
     * @param branchId   identificador de la sucursal
     * @param branchName nombre de la sucursal
     * @param topProduct producto con mayor stock (puede ser null)
     * @param products   lista completa de productos
     */
    public BranchWithProductsAndTopDTO(
            String branchId,
            String branchName,
            TopProductDTO topProduct,
            List<ProductDTO> products
    ) {
        this.branchId = branchId;
        this.branchName = branchName;
        this.topProduct = topProduct;
        this.products = products;
    }

    public String getBranchId() { return branchId; }
    public String getBranchName() { return branchName; }
    public TopProductDTO getTopProduct() { return topProduct; }
    public List<ProductDTO> getProducts() { return products; }
}