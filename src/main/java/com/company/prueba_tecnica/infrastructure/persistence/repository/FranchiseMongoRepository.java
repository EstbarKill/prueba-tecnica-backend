package com.company.prueba_tecnica.infrastructure.persistence.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import com.company.prueba_tecnica.infrastructure.persistence.document.FranchiseDocument;

import reactor.core.publisher.Mono;

public interface FranchiseMongoRepository extends ReactiveMongoRepository<FranchiseDocument, String> {
Mono<Boolean> existsById(String id);
Mono<Boolean> existsByName(String name);
Mono<Boolean> existsByNameAndIdNot(String name, String id);
}