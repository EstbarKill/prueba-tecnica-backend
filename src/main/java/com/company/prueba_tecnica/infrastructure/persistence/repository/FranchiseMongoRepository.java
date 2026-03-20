package com.company.prueba_tecnica.infrastructure.persistence.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import com.company.prueba_tecnica.infrastructure.persistence.document.FranchiseDocument;

public interface FranchiseMongoRepository extends ReactiveMongoRepository<FranchiseDocument, String> {
}