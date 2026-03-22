package com.company.prueba_tecnica.domain.model;

import lombok.*;

/**
 * Entidad de dominio que representa una Franquicia.
 *
 * Es la entidad raíz del modelo jerárquico:
 *   Franquicia → Sucursal (Branch) → Producto (Product)
 *
 * Lombok genera automáticamente:
 *   @Data            → getters, setters, equals, hashCode y toString
 *   @Builder         → patrón constructor fluido (Franchise.builder().id(...).build())
 *   @NoArgsConstructor → constructor vacío requerido por MongoDB
 *   @AllArgsConstructor → constructor con todos los campos
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Franchise {

    /** Identificador único de la franquicia. Mapeado como _id en MongoDB. */
    private String id;

    /** Nombre comercial de la franquicia. Debe ser único en todo el sistema. */
    private String name;

    /**
     * Cambia el nombre de la franquicia.
     *
     * Operación de dominio puro: no persiste por sí sola.
     * Debe invocarse antes de llamar a FranchiseRepository.save().
     *
     * @param name el nuevo nombre de la franquicia
     */
    public void rename(String name) {
        this.name = name;
    }
}
