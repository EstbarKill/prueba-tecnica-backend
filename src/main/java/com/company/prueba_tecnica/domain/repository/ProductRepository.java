package com.company.prueba_tecnica.domain.repository;

import com.company.prueba_tecnica.domain.model.Product;
import com.company.prueba_tecnica.domain.repository.ProductRepository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


public interface ProductRepository{
 Mono<Product> save(Product product);

    Mono<Product> findById(String id);
   
    Flux<Product> findByBranchId(String branchId);
   Mono<Boolean> existsByNameAndBranchId(String name, String branchId);
    Flux<Product> findAll();
Mono<Boolean> existsByIdAndBranchId(String productId, String branchId);

    Mono<Boolean> existsByNameAndBranchIdAndIdNot(String name, String branchId, String id);

    }