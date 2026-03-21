package com.company.prueba_tecnica.application.usecase.dto;

public class BranchTopProductDTO {

    private String branchId;
    private String branchName;
    private TopProductDTO topProduct;

    public BranchTopProductDTO(String branchId, String branchName, TopProductDTO topProduct) {
        this.branchId = branchId;
        this.branchName = branchName;
        this.topProduct = topProduct;
    }

    public String getBranchId() { return branchId; }
    public String getBranchName() { return branchName; }
    public TopProductDTO getTopProduct() { return topProduct; }
}