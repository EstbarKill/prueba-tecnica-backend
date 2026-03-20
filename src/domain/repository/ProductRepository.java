package com.company.prueba_tecnica.domain.repository;

import com.company.prueba_tecnica.domain.model.Product;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ProductRepository {
    Mono<Product> save(Product product);
    Mono<Void> deleteById(String id);
    Mono<Product> findById(String id);
    Flux<Product> findByBranchId(String branchId);
}