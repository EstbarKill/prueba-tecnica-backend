package com.company.prueba_tecnica.application.usecase;


import com.company.prueba_tecnica.domain.model.*;
import com.company.prueba_tecnica.domain.repository.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

class UpdateBranchNameUseCaseTest {

    private BranchRepository branchRepository;
    private ProductRepository productRepository;
    private UpdateBranchNameUseCase useCase;

    @BeforeEach
    void setUp() {
        branchRepository = Mockito.mock(BranchRepository.class);
        productRepository = Mockito.mock(ProductRepository.class);

        useCase = new UpdateBranchNameUseCase(branchRepository, productRepository);
    }

    @Test
    void shouldUpdateBranchName() {

        Branch branch = Branch.builder()
                .id("b1")
                .name("old")
                .franchiseId("f1")
                .build();

        Mockito.when(branchRepository.findById("b1"))
                .thenReturn(Mono.just(branch));

        Mockito.when(branchRepository.existsByNameAndFranchiseIdAndIdNot(
                Mockito.anyString(), Mockito.anyString(), Mockito.anyString()))
                .thenReturn(Mono.just(false));

        Mockito.when(branchRepository.save(Mockito.any()))
                .thenReturn(Mono.just(branch));

        Mockito.when(productRepository.findByBranchId("b1"))
                .thenReturn(Flux.empty());

        StepVerifier.create(useCase.execute("b1", new com.company.prueba_tecnica.application.usecase.dto.UpdateNameDTO()))
                .verifyComplete();
    }
}