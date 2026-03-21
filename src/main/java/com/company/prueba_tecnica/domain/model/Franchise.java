package com.company.prueba_tecnica.domain.model;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Franchise {

    private String id;
    private String name;

    public void rename(String name) {
        this.name = name;
    }
}