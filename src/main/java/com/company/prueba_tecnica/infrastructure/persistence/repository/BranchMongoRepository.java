package com.company.prueba_tecnica.infrastructure.persistence.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import com.company.prueba_tecnica.infrastructure.persistence.document.BranchDocument;

public interface BranchMongoRepository extends ReactiveMongoRepository<BranchDocument, String> {

    Flux<BranchDocument> findByFranchiseId(String franchiseId);
    Mono<Boolean> existsByNameAndFranchiseId(String name, String franchiseId);

    Mono<Boolean> existsByNameAndIdNot(String name, String id);

    Mono<Boolean> existsById(String id);

    Mono<Boolean> existsByNameAndFranchiseIdAndIdNot(String name, String franchiseId, String id);
}