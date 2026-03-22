package com.company.prueba_tecnica.application.usecase.dto;

import java.util.List;

public class FranchiseTopProductsDTO {

    private String id;
    private String name;
    private List<BranchTopProductDTO> branches;

    public FranchiseTopProductsDTO(String id, String name, List<BranchTopProductDTO> branches) {
        this.id = id;
        this.name = name;
        this.branches = branches;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public List<BranchTopProductDTO> getBranches() { return branches; }
}