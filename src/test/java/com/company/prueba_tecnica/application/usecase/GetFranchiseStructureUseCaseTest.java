package com.company.prueba_tecnica.application.usecase;


import com.company.prueba_tecnica.domain.model.Franchise;
import com.company.prueba_tecnica.domain.repository.FranchiseRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

class GetFranchiseStructureUseCaseTest {

    private FranchiseRepository franchiseRepository;
    private GetFranchiseStructureUseCase useCase;

    @BeforeEach
    void setUp() {
        franchiseRepository = Mockito.mock(FranchiseRepository.class);
        useCase = new GetFranchiseStructureUseCase(franchiseRepository, null, null);
    }

    @Test
    void shouldReturnAllFranchises() {

        Flux<Franchise> mockData = Flux.just(
                new Franchise("1", "Franquicia 1"),
                new Franchise("2", "Franquicia 2")
        );

        Mockito.when(franchiseRepository.findAll())
                .thenReturn(mockData);

        StepVerifier.create(useCase.execute())
                .expectNextCount(2)
                .verifyComplete();
    }

    @Test
    void shouldReturnEmptyWhenNoFranchises() {

        Mockito.when(franchiseRepository.findAll())
                .thenReturn(Flux.empty());

        StepVerifier.create(useCase.execute())
                .verifyComplete();
    }
}