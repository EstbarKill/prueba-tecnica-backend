package com.company.prueba_tecnica.application.usecase.dto;

public class TopProductByBranchDTO {

    private String branchId;
    private String branchName;
    private String productId;
    private String productName;
    private Integer stock;

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