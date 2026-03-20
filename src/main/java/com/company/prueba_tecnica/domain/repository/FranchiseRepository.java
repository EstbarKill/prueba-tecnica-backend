package com.company.prueba_tecnica.domain.repository;

import com.company.prueba_tecnica.domain.model.Franchise;
import reactor.core.publisher.Mono;

public interface FranchiseRepository {
    Mono<Franchise> save(Franchise franchise);
    Mono<Franchise> findById(String id);
}