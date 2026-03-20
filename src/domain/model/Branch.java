package com.company.prueba_tecnica.domain.model;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Branch {
    private String id;
    private String name;
    private String franchiseId;
}