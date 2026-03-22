package com.company.prueba_tecnica.infrastructure.persistence.document;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Documento MongoDB para la colección "branches".
 *
 * Representa la forma en que la sucursal se almacena en la base de datos.
 * Es el equivalente de infraestructura de la entidad de dominio Branch.
 *
 * franchiseId actúa como clave foránea lógica hacia la colección "franchises".
 * MongoDB no aplica integridad referencial; se gestiona en la capa de aplicación.
 */
@Document(collection = "branches")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BranchDocument {

    /**
     * Identificador único del documento en MongoDB.
     * @Id mapea este campo al campo _id de la colección.
     */
    @Id
    private String id;

    /** Nombre de la sucursal. */
    private String name;

    /**
     * Referencia a la franquicia propietaria.
     * Se usa para filtrar por franquicia en findByFranchiseId().
     */
    private String franchiseId;
}
