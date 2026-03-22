package com.company.prueba_tecnica.application.usecase.dto;


import java.util.List;

/**
 * DTO de respuesta para una Sucursal con su lista de productos.
 *
 * Se usa como parte anidada en FranchiseDTO y como respuesta directa en:
 *   - UpdateBranchNameUseCase
 */
public class BranchDTO {

    /** Identificador único de la sucursal. */
    private String id;

    /** Nombre de la sucursal. */
    private String name;

    /** Lista de productos que ofrece la sucursal. Puede estar vacía. */
    private List<ProductDTO> products;

    /**
     * Constructor completo requerido por los use cases.
     *
     * @param id       identificador de la sucursal
     * @param name     nombre de la sucursal
     * @param products lista de productos anidados
     */
    public BranchDTO(String id, String name, List<ProductDTO> products) {
        this.id = id;
        this.name = name;
        this.products = products;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public List<ProductDTO> getProducts() { return products; }
}