package com.company.prueba_tecnica.application.usecase;

import com.company.prueba_tecnica.domain.model.Branch;
import com.company.prueba_tecnica.domain.model.Franchise;
import com.company.prueba_tecnica.domain.repository.BranchRepository;
import com.company.prueba_tecnica.domain.repository.FranchiseRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

class CreateBranchUseCaseTest {

    private BranchRepository branchRepository;
    private FranchiseRepository franchiseRepository;

    private CreateBranchUseCase useCase;

    @BeforeEach
    void setUp() {
        branchRepository = Mockito.mock(BranchRepository.class);
        franchiseRepository = Mockito.mock(FranchiseRepository.class);

        useCase = new CreateBranchUseCase(branchRepository, franchiseRepository);
    }

    @Test
    void shouldCreateBranchSuccessfully() {

        String franchiseId = "fr-1";

        Franchise franchise = new Franchise(franchiseId, "Franquicia");

        Branch branch = Branch.builder()
                .id("br-1")
                .name("Sucursal")
                .franchiseId(franchiseId)
                .build();

        Mockito.when(franchiseRepository.findById(franchiseId))
                .thenReturn(Mono.just(franchise));

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
    void shouldFailWhenFranchiseNotExists() {

        String franchiseId = "invalid";

        Mockito.when(franchiseRepository.findById(franchiseId))
                .thenReturn(Mono.empty());

        Mono<Branch> result = useCase.execute("Sucursal", franchiseId);

        StepVerifier.create(result)
                .expectErrorMatches(error ->
                        error instanceof RuntimeException &&
                        error.getMessage().equals("Franchise not found")
                )
                .verify();
    }
}