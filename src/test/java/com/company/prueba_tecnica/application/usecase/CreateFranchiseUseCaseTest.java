package com.company.prueba_tecnica.application.usecase;

import com.company.prueba_tecnica.domain.exception.DuplicateResourceException;
import com.company.prueba_tecnica.domain.model.Franchise;
import com.company.prueba_tecnica.domain.repository.FranchiseRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

class CreateFranchiseUseCaseTest {

    private FranchiseRepository repository;
    private CreateFranchiseUseCase useCase;

    @BeforeEach
    void setUp() {
        repository = Mockito.mock(FranchiseRepository.class);
        useCase = new CreateFranchiseUseCase(repository);
    }

    @Test
    void shouldCreateFranchiseSuccessfully() {

        Franchise franchise = Franchise.builder()
                .id("fr-1")
                .name("Franquicia A")
                .build();

        Mockito.when(repository.existsById("fr-1"))
                .thenReturn(Mono.just(false));

        Mockito.when(repository.existsByName("Franquicia A"))
                .thenReturn(Mono.just(false));

        Mockito.when(repository.save(Mockito.any()))
                .thenReturn(Mono.just(franchise));

        Mono<Franchise> result = useCase.execute("fr-1", "Franquicia A");

        StepVerifier.create(result)
                .expectNextMatches(f ->
                        f.getId().equals("fr-1") &&
                        f.getName().equals("Franquicia A")
                )
                .verifyComplete();
    }

    @Test
    void shouldFailWhenIdAlreadyExists() {

        Mockito.when(repository.existsById("fr-1"))
                .thenReturn(Mono.just(true));

        Mono<Franchise> result = useCase.execute("fr-1", "Franquicia A");

        StepVerifier.create(result)
                .expectErrorMatches(error ->
                        error instanceof DuplicateResourceException &&
                        error.getMessage().equals("Franchise id already exists")
                )
                .verify();
    }

    @Test
    void shouldFailWhenNameAlreadyExists() {

        Mockito.when(repository.existsById("fr-1"))
                .thenReturn(Mono.just(false));

        Mockito.when(repository.existsByName("Franquicia A"))
                .thenReturn(Mono.just(true));

        Mono<Franchise> result = useCase.execute("fr-1", "Franquicia A");

        StepVerifier.create(result)
                .expectErrorMatches(error ->
                        error instanceof DuplicateResourceException &&
                        error.getMessage().equals("Franchise name already exists")
                )
                .verify();
    }
}