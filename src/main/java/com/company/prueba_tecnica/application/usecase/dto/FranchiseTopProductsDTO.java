package com.company.prueba_tecnica.application.usecase.dto;

import java.util.List;

/**
 * DTO raíz de respuesta para el endpoint top-products.
 *
 * Encapsula la franquicia consultada con la lista de sus sucursales,
 * donde cada sucursal incluye el producto de mayor stock.
 *
 * Es el tipo de retorno de GetTopProductByBranchInFranchiseUseCase.
 */
public class FranchiseTopProductsDTO {

    /** Identificador único de la franquicia. */
    private String id;
    
    /** Nombre de la franquicia. */
    private String name;

    /**
     * Lista de sucursales con su producto top incluido.
     * Cada elemento contiene la sucursal, su topProduct y todos sus productos.
     */
    private List<BranchWithProductsAndTopDTO> branches;

    /**
     * Constructor completo requerido por GetTopProductByBranchInFranchiseUseCase.
     *
     * @param id       identificador de la franquicia
     * @param name     nombre de la franquicia
     * @param branches lista de sucursales con sus top products
     */
    public FranchiseTopProductsDTO(String id, String name, List<BranchWithProductsAndTopDTO> branches) {
        this.id = id;
        this.name = name;
        this.branches = branches;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public List<BranchWithProductsAndTopDTO> getBranches() { return branches; }
}