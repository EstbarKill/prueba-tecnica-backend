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
}