package com.company.prueba_tecnica.domain.model;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Product {

    private String id;
    private String name;
    private Integer stock;
    private String branchId;

    public String getId() { return id; }
    public String getName() { return name; }
    public Integer getStock() { return stock; }

    public void rename(String name) {
        this.name = name;
    }

    public void changeStock(Integer newStock) {
        if (newStock == null || newStock < 0) {
            throw new IllegalArgumentException("Stock cannot be negative");
        }
        this.stock = newStock;
    }
}