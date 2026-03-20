package com.company.prueba_tecnica.application.usecase;

import com.company.prueba_tecnica.domain.model.Franchise;
import com.company.prueba_tecnica.domain.repository.FranchiseRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class CreateFranchiseUseCase {

    private final FranchiseRepository repository;

    public Mono<Franchise> execute(String id, String name ) {
        Franchise franchise = Franchise.builder()
                .id(id)
                .name(name)
                .build();

        return repository.save(franchise);
    }
}