package com.company.prueba_tecnica.infrastructure.persistence.document;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "franchises")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FranchiseDocument {

    @Id
    private String id;

    private String name;
}