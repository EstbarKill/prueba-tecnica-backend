package com.company.prueba_tecnica.application.usecase.dto;

/**
 * DTO que agrupa una sucursal con su producto de mayor stock.
 *
 * Versión simplificada de BranchWithProductsAndTopDTO: solo contiene
 * los datos básicos de la sucursal y el producto top, sin la lista completa.
 *
 * Nota: actualmente no se usa en ningún controlador activo.
 * La versión en uso es BranchWithProductsAndTopDTO.
 */
public class BranchTopProductDTO {

    /** Identificador de la sucursal. */
    private String branchId;

    /** Nombre de la sucursal. */
    private String branchName;
    
    /**
     * El producto con mayor stock en esta sucursal.
     * Puede ser null si la sucursal no tiene productos.
     */
    private TopProductDTO topProduct;

    /**
     * Constructor completo.
     *
     * @param branchId   identificador de la sucursal
     * @param branchName nombre de la sucursal
     * @param topProduct producto con mayor stock
     */
    public BranchTopProductDTO(String branchId, String branchName, TopProductDTO topProduct) {
        this.branchId = branchId;
        this.branchName = branchName;
        this.topProduct = topProduct;
    }

    public String getBranchId() { return branchId; }
    public String getBranchName() { return branchName; }
    public TopProductDTO getTopProduct() { return topProduct; }
}