package com.company.prueba_tecnica.application.usecase.dto;
import jakarta.validation.constraints.NotBlank;


public class UpdateNameDTO {

    @NotBlank(message = "Name cannot be empty")
    private String name;

    public UpdateNameDTO() {}

    public String getName() { return name; }
}