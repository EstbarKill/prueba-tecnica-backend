package com.company.prueba_tecnica.infrastructure.persistence.document;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Documento MongoDB para la colección "franchises".
 *
 * Representa la forma en que la franquicia se almacena en la base de datos.
 * Es el equivalente de infraestructura de la entidad de dominio Franchise.
 *
 * La separación entre Document y entidad de dominio permite que MongoDB
 * cambie su estructura sin afectar el núcleo del negocio.
 *
 * @Document(collection = "franchises") indica la colección de MongoDB.
 * Lombok genera constructores, getters, setters, equals y hashCode.
 */
@Document(collection = "franchises")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FranchiseDocument {

    /**
     * Identificador único del documento en MongoDB.
     * @Id mapea este campo al campo _id de la colección.
     */
    @Id
    private String id;

    /** Nombre de la franquicia almacenado en MongoDB. */
    private String name;
}
