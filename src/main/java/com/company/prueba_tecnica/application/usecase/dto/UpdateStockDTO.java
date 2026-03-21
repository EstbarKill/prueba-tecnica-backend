package com.company.prueba_tecnica.application.usecase.dto;

import jakarta.validation.constraints.NotBlank;

public class UpdateStockDTO {
    @NotBlank(message = "Name cannot be empty")
    private Integer stock;


    public UpdateStockDTO(Integer stock) {
        this.stock = stock;
    }

    public Integer getStock() { return stock; }
}