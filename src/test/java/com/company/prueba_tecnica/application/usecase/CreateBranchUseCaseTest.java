package com.company.prueba_tecnica.application.usecase;

import com.company.prueba_tecnica.domain.exception.DuplicateResourceException;
import com.company.prueba_tecnica.domain.model.Branch;
import com.company.prueba_tecnica.domain.repository.BranchRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

class CreateBranchUseCaseTest {

    private BranchRepository branchRepository;
    private CreateBranchUseCase useCase;

    @BeforeEach
    void setUp() {
        branchRepository = Mockito.mock(BranchRepository.class);

        useCase = new CreateBranchUseCase(branchRepository);
    }

    @Test
    void shouldCreateBranchSuccessfully() {

        String franchiseId = "fr-1";

        Branch branch = Branch.builder()
                .id("br-1")
                .name("Sucursal")
                .franchiseId(franchiseId)
                .build();

        Mockito.when(branchRepository.existsByNameAndFranchiseId("Sucursal", franchiseId))
                .thenReturn(Mono.just(false));

        Mockito.when(branchRepository.save(Mockito.any()))
                .thenReturn(Mono.just(branch));

        Mono<Branch> result = useCase.execute("Sucursal", franchiseId);

        StepVerifier.create(result)
                .expectNextMatches(b ->
                        b.getName().equals("Sucursal") &&
                        b.getFranchiseId().equals(franchiseId)
                )
                .verifyComplete();
    }

    @Test
    void shouldFailWhenBranchAlreadyExists() {

        String franchiseId = "fr-1";

        Mockito.when(branchRepository.existsByNameAndFranchiseId("Sucursal", franchiseId))
                .thenReturn(Mono.just(true));

        Mono<Branch> result = useCase.execute("Sucursal", franchiseId);

        StepVerifier.create(result)
                .expectErrorMatches(error ->
                        error instanceof DuplicateResourceException &&
                        error.getMessage().equals("Branch name already exists in this franchise")
                )
                .verify();
    }
}