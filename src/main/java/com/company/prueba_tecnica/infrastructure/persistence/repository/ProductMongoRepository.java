package com.company.prueba_tecnica.infrastructure.persistence.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import com.company.prueba_tecnica.infrastructure.persistence.document.ProductDocument;

/**
 * Repositorio reactivo de MongoDB para la colección "products".
 *
 * Extiende ReactiveMongoRepository, que provee operaciones CRUD estándar
 * de forma no bloqueante (save, findById, findAll, deleteById, etc.).
 *
 * Los métodos adicionales usan Query Derivation de Spring Data:
 * Spring genera automáticamente la consulta MongoDB a partir del nombre del método.
 *
 * Implementado internamente por Spring Data; no requiere clase concreta.
 */
public interface ProductMongoRepository extends ReactiveMongoRepository<ProductDocument, String> {

    /**
     * Retorna todos los productos que pertenecen a una sucursal.
     * Query derivada: db.products.find({branchId: branchId})
     *
     * @param branchId ID de la sucursal propietaria
     * @return Flux con los productos encontrados
     */
    Flux<ProductDocument> findByBranchId(String branchId);

    /**
     * Elimina todos los productos de una sucursal en una sola operación.
     * Query derivada: db.products.deleteMany({branchId: branchId})
     *
     * Usado en BranchRepositoryAdapter.deleteBranchWithProducts()
     * para eliminar en cascada cuando se borra una sucursal.
     *
     * @param branchId ID de la sucursal cuyos productos se eliminarán
     * @return Mono vacío al completarse
     */
    Mono<Void> deleteByBranchId(String branchId);

    /**
     * Verifica si existe un producto con ese ID perteneciente a una sucursal.
     * Query derivada: db.products.countDocuments({_id: productId, branchId: branchId}) > 0
     *
     * Usado en DeleteProductsByBranchUseCase para confirmar que el producto
     * realmente pertenece a la sucursal antes de eliminarlo.
     *
     * @param productId ID del producto
     * @param branchId  ID de la sucursal
     * @return Mono con true si el producto existe y pertenece a esa sucursal
     */
    Mono<Boolean> existsByIdAndBranchId(String productId, String branchId);

    /**
     * Verifica si existe un producto con ese nombre en una sucursal.
     * Query derivada: db.products.countDocuments({name: name, branchId: branchId}) > 0
     *
     * Usado en CreateProductUseCase para evitar nombres duplicados.
     *
     * @param name     nombre a verificar
     * @param branchId ID de la sucursal donde se busca
     * @return Mono con true si ya existe, false si está disponible
     */
    Mono<Boolean> existsByNameAndBranchId(String name, String branchId);

    /**
     * Verifica si existe otro producto con ese nombre en la sucursal,
     * excluyendo un ID concreto.
     * Query derivada: db.products.countDocuments({name, branchId, _id: {$ne: id}}) > 0
     *
     * Usado en UpdateProductNameUseCase para validar el nuevo nombre
     * sin rechazar el producto que se está editando.
     *
     * @param name     nombre a verificar
     * @param branchId ID de la sucursal donde se busca
     * @param id       ID del producto a excluir
     * @return Mono con true si otro documento ya tiene ese nombre en esa sucursal
     */
    Mono<Boolean> existsByNameAndBranchIdAndIdNot(String name, String branchId, String id);

    /**
     * Elimina un producto por su ID.
     * Redeclara el método heredado de ReactiveMongoRepository para que
     * DeleteProductsByBranchUseCase pueda invocarlo explícitamente.
     *
     * @param id ID del producto a eliminar
     * @return Mono vacío al completarse
     */
    Mono<Void> deleteById(String id);
}
