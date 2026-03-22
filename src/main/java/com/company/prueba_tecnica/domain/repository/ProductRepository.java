package com.company.prueba_tecnica.domain.repository;

import com.company.prueba_tecnica.domain.model.Product;
import com.company.prueba_tecnica.domain.repository.ProductRepository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Contrato de persistencia para la entidad Product (Producto).
 *
 * Define las operaciones que la capa de infraestructura debe implementar.
 * El dominio no conoce MongoDB ni ninguna tecnología de persistencia concreta.
 *
 * Implementado por: ProductRepositoryAdapter
 */
public interface ProductRepository {

    /**
     * Persiste un producto nuevo o actualiza uno existente.
     *
     * @param product entidad a guardar
     * @return Mono con el producto guardado
     */
    Mono<Product> save(Product product);

    /**
     * Busca un producto por su identificador único.
     *
     * @param id identificador del producto
     * @return Mono con el producto encontrado, o Mono vacío si no existe
     */
    Mono<Product> findById(String id);

    /**
     * Retorna todos los productos pertenecientes a una sucursal.
     *
     * @param branchId ID de la sucursal propietaria
     * @return Flux con los productos encontrados (puede estar vacío)
     */
    Flux<Product> findByBranchId(String branchId);

    /**
     * Verifica si ya existe un producto con ese nombre dentro de una sucursal.
     * Usado en CreateProductUseCase para evitar nombres duplicados.
     *
     * @param name     nombre del producto a verificar
     * @param branchId ID de la sucursal donde se busca
     * @return Mono con true si ya existe, false si está disponible
     */
    Mono<Boolean> existsByNameAndBranchId(String name, String branchId);

    /**
     * Retorna todos los productos del sistema.
     *
     * @return Flux con todos los productos
     */
    Flux<Product> findAll();

    /**
     * Verifica si existe un producto con ese ID perteneciente a una sucursal.
     * Usado en DeleteProductsByBranchUseCase para confirmar que el producto
     * realmente pertenece a la sucursal indicada antes de eliminarlo.
     *
     * @param productId ID del producto
     * @param branchId  ID de la sucursal
     * @return Mono con true si el producto existe y pertenece a esa sucursal
     */
    Mono<Boolean> existsByIdAndBranchId(String productId, String branchId);

    /**
     * Verifica si otro producto tiene el mismo nombre dentro de la sucursal,
     * excluyendo un ID. Usado en UpdateProductNameUseCase para validar
     * el nuevo nombre sin rechazar el producto que se está editando.
     *
     * @param name     nombre a verificar
     * @param branchId ID de la sucursal donde se busca
     * @param id       ID del producto que se excluye de la búsqueda
     * @return Mono con true si otro documento ya tiene ese nombre
     */
    Mono<Boolean> existsByNameAndBranchIdAndIdNot(String name, String branchId, String id);
}
