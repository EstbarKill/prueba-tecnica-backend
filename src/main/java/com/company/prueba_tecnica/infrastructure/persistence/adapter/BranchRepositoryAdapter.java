package com.company.prueba_tecnica.infrastructure.persistence.adapter;

import com.company.prueba_tecnica.domain.model.Branch;
import com.company.prueba_tecnica.domain.repository.BranchRepository;
import com.company.prueba_tecnica.infrastructure.persistence.document.BranchDocument;
import com.company.prueba_tecnica.infrastructure.persistence.repository.BranchMongoRepository;
import com.company.prueba_tecnica.infrastructure.persistence.repository.ProductMongoRepository;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class BranchRepositoryAdapter implements BranchRepository {

    private final BranchMongoRepository repository;
    private final ProductMongoRepository productRepository;

    @Override
    public Mono<Branch> findById(String id) {
        return repository.findById(id)
                .map(doc -> new Branch(
                        doc.getId(),
                        doc.getName(),
                        doc.getFranchiseId()
                ));
    }

    @Override
    public Mono<Branch> save(Branch branch) {
        BranchDocument doc = BranchDocument.builder()
                .id(branch.getId())
                .name(branch.getName())
                .franchiseId(branch.getFranchiseId())
                .build();

        return repository.save(doc)
                .map(saved -> new Branch(
                        saved.getId(),
                        saved.getName(),
                        saved.getFranchiseId()
                ));
    }

    @Override
    public Flux<Branch> findByFranchiseId(String franchiseId) {
        return repository.findByFranchiseId(franchiseId)
                .map(doc -> new Branch(
                        doc.getId(),
                        doc.getName(),
                        doc.getFranchiseId()
                ));
    }

    @Override
    public Flux<Branch> findAll() {
        return repository.findAll()
                .map(doc -> new Branch(
                        doc.getId(),
                        doc.getName(),
                        doc.getFranchiseId()
                ));
    }
@Override
public Mono<Void> deleteBranchWithProducts(String branchId) {

    return repository.findById(branchId)
            .switchIfEmpty(Mono.error(new RuntimeException("Branch not found")))
            .then(productRepository.deleteByBranchId(branchId)) // 🔥 masivo
            .then(repository.deleteById(branchId));
}
}