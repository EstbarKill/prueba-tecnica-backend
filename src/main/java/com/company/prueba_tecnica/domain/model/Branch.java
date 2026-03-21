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

    public String getId() { return id; }
    public String getName() { return name; }

    public void rename(String name) {
        this.name = name;
    }   
}