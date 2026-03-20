package com.company.prueba_tecnica.application.usecase;

import com.company.prueba_tecnica.domain.model.Franchise;
import com.company.prueba_tecnica.domain.repository.FranchiseRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateFranchiseUseCase {

    private final FranchiseRepository franchiseRepository;

    public Mono<Franchise> execute(String name) {
        Franchise franchise = Franchise.builder()
                .name(name)
                .build();

        return franchiseRepository.save(franchise);
    }
}