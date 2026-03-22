package com.company.prueba_tecnica.application.usecase.dto;

public class TopProductDTO {

    private String productId;
    private String productName;
    private Integer stock;

    public TopProductDTO(String productId, String productName, Integer stock) {
        this.productId = productId;
        this.productName = productName;
        this.stock = stock;
    }

    public String getProductId() { return productId; }
    public String getProductName() { return productName; }
    public Integer getStock() { return stock; }
}