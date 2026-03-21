package com.company.prueba_tecnica.application.usecase.dto;

public class ProductDTO {

    private String id;
    private String name;
    private Integer stock;

    public ProductDTO(String id, String name, Integer stock) {
        this.id = id;
        this.name = name;
        this.stock = stock;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public Integer getStock() { return stock; }
}
