package com.company.prueba_tecnica.infrastructure.persistence.adapter;

import com.company.prueba_tecnica.domain.model.Franchise;
import com.company.prueba_tecnica.domain.repository.FranchiseRepository;
import com.company.prueba_tecnica.infrastructure.persistence.document.FranchiseDocument;
import com.company.prueba_tecnica.infrastructure.persistence.repository.FranchiseMongoRepository;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class FranchiseRepositoryAdapter implements FranchiseRepository {

    private final FranchiseMongoRepository repository;

    @Override
    public Mono<Franchise> save(Franchise franchise) {
        FranchiseDocument doc = FranchiseDocument.builder()
                .id(franchise.getId())
                .name(franchise.getName())
                .build();

        return repository.save(doc)
                .map(saved -> new Franchise(
                        saved.getId(),
                        saved.getName()
                ));
    }

    @Override
    public Mono<Franchise> findById(String id) {
        return repository.findById(id)
                .map(doc -> new Franchise(
                        doc.getId(),
                        doc.getName()
                ));
    }

@Override
public Flux<Franchise> findAll() {
    return repository.findAll()
            .map(doc -> new Franchise(
                    doc.getId(),
                    doc.getName()
            ));
}
}