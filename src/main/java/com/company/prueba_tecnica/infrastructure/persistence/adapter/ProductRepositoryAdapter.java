package com.company.prueba_tecnica.infrastructure.persistence.adapter;

import com.company.prueba_tecnica.domain.model.Product;
import com.company.prueba_tecnica.domain.repository.ProductRepository;
import com.company.prueba_tecnica.infrastructure.persistence.document.ProductDocument;
import com.company.prueba_tecnica.infrastructure.persistence.repository.ProductMongoRepository;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import org.springframework.stereotype.Repository;

/**
 * Adaptador que implementa el contrato ProductRepository del dominio
 * usando ProductMongoRepository de Spring Data.
 *
 * Responsabilidades:
 *   1. Traducir llamadas del dominio a operaciones de MongoDB
 *   2. Mapear ProductDocument (infraestructura) ↔ Product (dominio)
 */
@Repository
@RequiredArgsConstructor
public class ProductRepositoryAdapter implements ProductRepository {

    /** Repositorio de Spring Data para la colección "products". */
    private final ProductMongoRepository repository;

    /**
     * Persiste un producto convirtiéndolo primero a Document.
     *
     * Flujo: Product (dominio) → ProductDocument → MongoDB → Product (dominio)
     *
     * @param product entidad de dominio a guardar
     * @return Mono con el producto guardado
     */
    @Override
    public Mono<Product> save(Product product) {
        // Convertir entidad de dominio a documento de MongoDB
        ProductDocument doc = ProductDocument.builder()
                .id(product.getId())
                .name(product.getName())
                .stock(product.getStock())
                .branchId(product.getBranchId())
                .build();

        // Guardar en MongoDB y convertir el resultado de vuelta a dominio
        return repository.save(doc)
                .map(saved -> new Product(
                        saved.getId(),
                        saved.getName(),
                        saved.getStock(),
                        saved.getBranchId()
                ));
    }

    /**
     * Busca un producto por ID y lo convierte al modelo de dominio.
     *
     * @param id identificador del producto
     * @return Mono con el producto, o Mono vacío si no existe
     */
    @Override
    public Mono<Product> findById(String id) {
        return repository.findById(id)
                .map(doc -> new Product(
                        doc.getId(),
                        doc.getName(),
                        doc.getStock(),
                        doc.getBranchId()
                ));
    }

    /**
     * Retorna todos los productos de una sucursal convertidos al modelo de dominio.
     *
     * @param branchId ID de la sucursal propietaria
     * @return Flux con los productos encontrados
     */
    @Override
    public Flux<Product> findByBranchId(String branchId) {
        return repository.findByBranchId(branchId)
                .map(doc -> new Product(
                        doc.getId(),
                        doc.getName(),
                        doc.getStock(),
                        doc.getBranchId()
                ));
    }

    /**
     * Retorna todos los productos del sistema convertidos al modelo de dominio.
     *
     * @return Flux con todos los productos
     */
    @Override
    public Flux<Product> findAll() {
        return repository.findAll()
                .map(doc -> new Product(
                        doc.getId(),
                        doc.getName(),
                        doc.getStock(),
                        doc.getBranchId()
                ));
    }

    /** {@inheritDoc} */
    @Override
    public Mono<Boolean> existsByNameAndBranchId(String name, String branchId) {
        return repository.existsByNameAndBranchId(name, branchId);
    }

    /** {@inheritDoc} */
    @Override
    public Mono<Boolean> existsByNameAndBranchIdAndIdNot(String name, String branchId, String id) {
        return repository.existsByNameAndBranchIdAndIdNot(name, branchId, id);
    }

    /** {@inheritDoc} */
    @Override
    public Mono<Boolean> existsByIdAndBranchId(String productId, String branchId) {
        return repository.existsByIdAndBranchId(productId, branchId);
    }
}
