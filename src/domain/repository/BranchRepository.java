package com.company.prueba_tecnica.domain.repository;

import com.company.prueba_tecnica.domain.model.Branch;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface BranchRepository {
    Mono<Branch> save(Branch branch);
    Flux<Branch> findByFranchiseId(String franchiseId);
}