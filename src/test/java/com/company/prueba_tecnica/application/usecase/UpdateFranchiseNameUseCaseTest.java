package com.company.prueba_tecnica.application.usecase;


import com.company.prueba_tecnica.domain.model.*;
import com.company.prueba_tecnica.domain.repository.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

class UpdateFranchiseNameUseCaseTest {

    private FranchiseRepository franchiseRepository;
    private BranchRepository branchRepository;
    private ProductRepository productRepository;

    private UpdateFranchiseNameUseCase useCase;

    @BeforeEach
    void setUp() {
        franchiseRepository = Mockito.mock(FranchiseRepository.class);
        branchRepository = Mockito.mock(BranchRepository.class);
        productRepository = Mockito.mock(ProductRepository.class);

        useCase = new UpdateFranchiseNameUseCase(
                franchiseRepository,
                branchRepository,
                productRepository
        );
    }

    @Test
    void shouldUpdateFranchise() {

        Franchise franchise = Franchise.builder()
                .id("f1")
                .name("old")
                .build();

        Mockito.when(franchiseRepository.findById("f1"))
                .thenReturn(Mono.just(franchise));

        Mockito.when(franchiseRepository.existsByNameAndIdNot(Mockito.anyString(), Mockito.anyString()))
                .thenReturn(Mono.just(false));

        Mockito.when(franchiseRepository.save(Mockito.any()))
                .thenReturn(Mono.just(franchise));

        Mockito.when(branchRepository.findByFranchiseId("f1"))
                .thenReturn(Flux.empty());

        StepVerifier.create(useCase.execute("f1", new com.company.prueba_tecnica.application.usecase.dto.UpdateNameDTO()))
                .verifyComplete();
    }
}