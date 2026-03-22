package com.company.prueba_tecnica.domain.model;

import lombok.*;

/**
 * Entidad de dominio que representa una Sucursal.
 *
 * Una sucursal pertenece a exactamente una franquicia (referenciada por franchiseId)
 * y puede contener múltiples productos.
 *
 * Lombok genera automáticamente:
 *   @Data            → getters, setters, equals, hashCode y toString
 *   @AllArgsConstructor → constructor con todos los campos
 *   @NoArgsConstructor  → constructor vacío requerido por MongoDB / frameworks
 *   @Builder         → patrón constructor fluido
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Branch {

    /** Identificador único de la sucursal. Generado por MongoDB (ObjectId como String). */
    private String id;

    /** Nombre de la sucursal. Debe ser único dentro de la misma franquicia. */
    private String name;

    /**
     * Referencia a la franquicia propietaria de esta sucursal.
     * Actúa como clave foránea lógica (no hay joins en MongoDB).
     */
    private String franchiseId;

    // Lombok genera getId() y getName() pero se declaran explícitamente
    // para dejar claros los accesores públicos en el dominio.
    public String getId() { return id; }
    public String getName() { return name; }

    /**
     * Cambia el nombre de la sucursal.
     *
     * Operación de dominio puro: no persiste por sí sola.
     * Debe invocarse antes de llamar a BranchRepository.save().
     *
     * @param name el nuevo nombre de la sucursal
     */
    public void rename(String name) {
        this.name = name;
    }
}
