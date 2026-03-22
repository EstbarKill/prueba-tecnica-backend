package com.company.prueba_tecnica.domain.repository;

import com.company.prueba_tecnica.domain.model.Franchise;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface FranchiseRepository {
    Mono<Franchise> save(Franchise franchise);
    
    Mono<Franchise> findById(String id);
    
    Flux<Franchise> findAll();

        Mono<Boolean> existsById(String id);

    Mono<Boolean> existsByName(String name);

    Mono<Boolean> existsByNameAndIdNot(String name, String id);
}