package com.company.prueba_tecnica.application.usecase.dto;


import java.util.List;

public class BranchDTO {

    private String id;
    private String name;
    private List<ProductDTO> products;

    public BranchDTO(String id, String name, List<ProductDTO> products) {
        this.id = id;
        this.name = name;
        this.products = products;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public List<ProductDTO> getProducts() { return products; }
}