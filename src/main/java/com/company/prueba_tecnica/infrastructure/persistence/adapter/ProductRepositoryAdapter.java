package com.company.prueba_tecnica.infrastructure.persistence.adapter;

import com.company.prueba_tecnica.domain.model.Product;
import com.company.prueba_tecnica.domain.repository.ProductRepository;
import com.company.prueba_tecnica.infrastructure.persistence.document.ProductDocument;
import com.company.prueba_tecnica.infrastructure.persistence.repository.ProductMongoRepository;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ProductRepositoryAdapter implements ProductRepository {

    private final ProductMongoRepository repository;

    @Override
    public Mono<Product> save(Product product) {
        ProductDocument doc = ProductDocument.builder()
                .id(product.getId())
                .name(product.getName())
                .stock(product.getStock())
                .branchId(product.getBranchId())
                .build();

        return repository.save(doc)
                .map(saved -> new Product(
                        saved.getId(),
                        saved.getName(),
                        saved.getStock(),
                        saved.getBranchId()
                ));
    }

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
}