package com.company.prueba_tecnica.application.usecase;

import com.company.prueba_tecnica.domain.exception.NotFoundException;
import com.company.prueba_tecnica.infrastructure.persistence.repository.ProductMongoRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

/**
 * Tests unitarios para DeleteProductsByBranchUseCase.
 *
 * Verifica que:
 *   - Un producto se elimina correctamente cuando existe en la sucursal
 *   - Se lanza NotFoundException cuando el producto no pertenece a la sucursal
 */
class DeleteProductsByBranchUseCaseTest {

    private ProductMongoRepository productRepository;
    private DeleteProductsByBranchUseCase useCase;

    @BeforeEach
    void setUp() {
        productRepository = Mockito.mock(ProductMongoRepository.class);
        useCase = new DeleteProductsByBranchUseCase(productRepository);
    }

    /**
     * Caso feliz: el producto existe en la sucursal → se elimina y el Mono completa.
     */
    @Test
    void shouldDeleteProductSuccessfully() {

        // El producto "p1" pertenece a la sucursal "b1"
        Mockito.when(productRepository.existsByIdAndBranchId("p1", "b1"))
                .thenReturn(Mono.just(true));

        // deleteById retorna Mono<Void> vacío al completarse
        Mockito.when(productRepository.deleteById("p1"))
                .thenReturn(Mono.empty());

        // verifyComplete() asegura que el Mono completó sin errores ni valores
        StepVerifier.create(useCase.execute("b1", "p1"))
                .verifyComplete();
    }

    /**
     * El producto no existe en la sucursal → debe emitir NotFoundException.
     */
    @Test
    void shouldFailWhenProductNotFound() {

        // El producto no pertenece a esta sucursal
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
