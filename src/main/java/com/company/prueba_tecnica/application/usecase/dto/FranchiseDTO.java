package com.company.prueba_tecnica.application.usecase.dto;

import java.util.List;

public class FranchiseDTO {

    private String id;
    private String name;
    private List<BranchDTO> branches;

    public FranchiseDTO(String id, String name, List<BranchDTO> branches) {
        this.id = id;
        this.name = name;
        this.branches = branches;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public List<BranchDTO> getBranches() { return branches; }
}