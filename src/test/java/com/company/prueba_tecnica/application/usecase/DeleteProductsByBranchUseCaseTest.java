package com.company.prueba_tecnica.application.usecase;

import com.company.prueba_tecnica.domain.exception.NotFoundException;
import com.company.prueba_tecnica.infrastructure.persistence.repository.ProductMongoRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

class DeleteProductsByBranchUseCaseTest {

    private ProductMongoRepository productRepository;
    private DeleteProductsByBranchUseCase useCase;

    @BeforeEach
    void setUp() {
        productRepository = Mockito.mock(ProductMongoRepository.class);
        useCase = new DeleteProductsByBranchUseCase(productRepository);
    }

    @Test
    void shouldDeleteProductSuccessfully() {

        Mockito.when(productRepository.existsByIdAndBranchId("p1", "b1"))
                .thenReturn(Mono.just(true));

        Mockito.when(productRepository.deleteById("p1"))
                .thenReturn(Mono.empty());

        StepVerifier.create(useCase.execute("b1", "p1"))
                .verifyComplete();
    }

    @Test
    void shouldFailWhenProductNotFound() {

        Mockito.when(productRepository.existsByIdAndBranchId("p1", "b1"))
                .thenReturn(Mono.just(false));

        StepVerifier.create(useCase.execute("b1", "p1"))
                .expectErrorMatches(e ->
                        e instanceof NotFoundException &&
                        e.getMessage().contains("not found")
                )
                .verify();
    }
}