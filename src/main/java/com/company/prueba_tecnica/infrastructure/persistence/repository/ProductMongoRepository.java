package com.company.prueba_tecnica.infrastructure.persistence.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import com.company.prueba_tecnica.infrastructure.persistence.document.ProductDocument;

public interface ProductMongoRepository extends ReactiveMongoRepository<ProductDocument, String> {

    Flux<ProductDocument> findByBranchId(String branchId);
        Mono<Void> deleteByBranchId(String branchId);
        Mono<Boolean> existsByIdAndBranchId(String productId, String branchId);

Mono<Void> deleteById(String id);

}