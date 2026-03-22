package com.company.prueba_tecnica.infrastructure.persistence.document;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Documento MongoDB para la colección "products".
 *
 * Representa la forma en que el producto se almacena en la base de datos.
 * Es el equivalente de infraestructura de la entidad de dominio Product.
 *
 * branchId actúa como clave foránea lógica hacia la colección "branches".
 * La eliminación en cascada de productos al borrar una sucursal se gestiona
 * manualmente en BranchRepositoryAdapter.deleteBranchWithProducts().
 */
@Document(collection = "products")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductDocument {

    /**
     * Identificador único del documento en MongoDB.
     * @Id mapea este campo al campo _id de la colección.
     */
    @Id
    private String id;

    /** Nombre del producto. */
    private String name;

    /** Cantidad disponible en inventario. */
    private Integer stock;

    /**
     * Referencia a la sucursal propietaria.
     * Se usa para filtrar por sucursal en findByBranchId()
     * y para eliminar en cascada con deleteByBranchId().
     */
    private String branchId;
}
