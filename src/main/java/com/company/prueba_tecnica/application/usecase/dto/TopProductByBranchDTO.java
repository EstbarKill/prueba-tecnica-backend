package com.company.prueba_tecnica.application.usecase.dto;
    
    /**
 * DTO plano que combina datos de la sucursal y su producto top en un solo objeto.
 *
 * A diferencia de BranchWithProductsAndTopDTO, es una estructura plana
 * (sin objetos anidados) que contiene todos los campos en un mismo nivel.
 *
 * Nota: actualmente no se usa en ningún controlador activo.
 */
public class TopProductByBranchDTO {

    /** Identificador de la sucursal. */
    private String branchId;
    /** Nombre de la sucursal. */
    private String branchName;
    /** Identificador del producto con mayor stock. */
    private String productId;
    /** Nombre del producto con mayor stock. */
    private String productName;
    /** Stock del producto con mayor stock. */
    private Integer stock;

    /**
     * Constructor completo.
     *
     * @param branchId    identificador de la sucursal
     * @param branchName  nombre de la sucursal
     * @param productId   identificador del producto top
     * @param productName nombre del producto top
     * @param stock       stock del producto top
     */
    public TopProductByBranchDTO(
            String branchId,
            String branchName,
            String productId,
            String productName,
            Integer stock) {
        this.branchId    = branchId;
        this.branchName  = branchName;
        this.productId   = productId;
        this.productName = productName;
        this.stock       = stock;
    }

    public String getBranchId()    { return branchId; }
    public String getBranchName()  { return branchName; }
    public String getProductId()   { return productId; }
    public String getProductName() { return productName; }
    public Integer getStock()      { return stock; }
}