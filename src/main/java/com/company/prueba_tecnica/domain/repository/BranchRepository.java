package com.company.prueba_tecnica.domain.repository;

import com.company.prueba_tecnica.domain.model.Branch;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface BranchRepository {
    Mono<Branch> findById(String id);
    Mono<Branch> save(Branch branch);
    Flux<Branch> findByFranchiseId(String franchiseId);
    Flux<Branch> findAll();
    Mono<Void> deleteBranchWithProducts(String branchId);
Mono<Boolean> existsById(String id);
Mono<Boolean> existsByNameAndFranchiseId(String name, String franchiseId);
Mono<Boolean> existsByNameAndFranchiseIdAndIdNot(String name, String franchiseId, String id);
}