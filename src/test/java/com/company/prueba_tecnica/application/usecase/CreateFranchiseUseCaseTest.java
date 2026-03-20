package com.company.prueba_tecnica.application.usecase;

import com.company.prueba_tecnica.domain.model.Franchise;
import com.company.prueba_tecnica.domain.repository.FranchiseRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

class CreateFranchiseUseCaseTest {

    private FranchiseRepository franchiseRepository;
    private CreateFranchiseUseCase useCase;

    @BeforeEach
    void setUp() {
        franchiseRepository = Mockito.mock(FranchiseRepository.class);
        useCase = new CreateFranchiseUseCase(franchiseRepository);
    }

    @Test
    void shouldCreateFranchiseSuccessfully() {

        Franchise franchise = new Franchise("1", "Franquicia Test");

        Mockito.when(franchiseRepository.save(Mockito.any()))
                .thenReturn(Mono.just(franchise));

        Mono<Franchise> result = useCase.execute("Franquicia Test", null);

        StepVerifier.create(result)
                .expectNextMatches(f ->
                        f.getName().equals("Franquicia Test")
                )
                .verifyComplete();
    }
}