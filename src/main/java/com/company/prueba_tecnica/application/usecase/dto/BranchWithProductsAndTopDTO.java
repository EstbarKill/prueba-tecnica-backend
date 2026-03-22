package com.company.prueba_tecnica.application.usecase.dto;

import java.util.List;

public class BranchWithProductsAndTopDTO {

    private String branchId;
    private String branchName;
    private TopProductDTO topProduct;
    private List<ProductDTO> products;

    public BranchWithProductsAndTopDTO(
            String branchId,
            String branchName,
            TopProductDTO topProduct,
            List<ProductDTO> products
    ) {
        this.branchId = branchId;
        this.branchName = branchName;
        this.topProduct = topProduct;
        this.products = products;
    }

    public String getBranchId() { return branchId; }
    public String getBranchName() { return branchName; }
    public TopProductDTO getTopProduct() { return topProduct; }
    public List<ProductDTO> getProducts() { return products; }
}