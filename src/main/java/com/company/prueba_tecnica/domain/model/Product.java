package com.company.prueba_tecnica.domain.model;

import lombok.*;

/**
 * Entidad de dominio que representa un Producto.
 *
 * Un producto pertenece a exactamente una sucursal (referenciada por branchId)
 * y tiene un nombre y una cantidad de stock disponible.
 *
 * Esta entidad encapsula reglas de negocio relacionadas con el stock:
 * no se permiten valores negativos ni nulos.
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
public class Product {

    /** Identificador único del producto. Generado por MongoDB (ObjectId como String). */
    private String id;

    /** Nombre del producto. Debe ser único dentro de la misma sucursal. */
    private String name;

    /** Cantidad disponible en inventario. Siempre mayor o igual a cero. */
    private Integer stock;

    /**
     * Referencia a la sucursal propietaria de este producto.
     * Actúa como clave foránea lógica.
     */
    private String branchId;

    // Lombok genera estos getters, pero se declaran explícitamente
    // para visibilidad del contrato público de la entidad.
    public String getId() { return id; }
    public String getName() { return name; }
    public Integer getStock() { return stock; }

    /**
     * Cambia el nombre del producto.
     *
     * Operación de dominio puro. Debe invocarse antes de ProductRepository.save().
     *
     * @param name el nuevo nombre del producto
     */
    public void rename(String name) {
        this.name = name;
    }

    /**
     * Actualiza el stock del producto con validación estricta.
     *
     * Lanza excepción si el valor es null o negativo, garantizando
     * la invariante de negocio: el stock nunca puede ser negativo.
     *
     * @param newStock nuevo valor de stock
     * @throws IllegalArgumentException si newStock es null o menor a 0
     */
    public void changeStock(Integer newStock) {
        if (newStock == null || newStock < 0) {
            throw new IllegalArgumentException("Stock cannot be negative");
        }
        this.stock = newStock;
    }

    /**
     * Actualiza el stock del producto.
     *
     * Equivalente a changeStock() pero sin validación de nulo.
     * Usado por UpdateProductStockUseCase.
     *
     * @param stock nuevo valor de stock
     * @throws IllegalArgumentException si stock es menor a 0
     */
    public void updateStock(Integer stock) {
        if (stock < 0) {
            throw new IllegalArgumentException("Stock cannot be negative");
        }
        this.stock = stock;
    }
}
