package com.company.prueba_tecnica.infrastructure.persistence.document;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "products")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductDocument {

    @Id
    private String id;

    private String name;

    private Integer stock;

    private String branchId;
}