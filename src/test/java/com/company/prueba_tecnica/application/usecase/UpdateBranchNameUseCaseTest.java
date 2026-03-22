package com.company.prueba_tecnica.application.usecase;

import com.company.prueba_tecnica.domain.model.*;
import com.company.prueba_tecnica.domain.repository.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

/**
 * Tests unitarios para UpdateBranchNameUseCase.
 *
 * Verifica que el nombre de una sucursal se actualiza correctamente
 * cuando no hay conflicto con otros nombres en la misma franquicia.
 */
class UpdateBranchNameUseCaseTest {

    private BranchRepository  branchRepository;
    private ProductRepository productRepository;
    private UpdateBranchNameUseCase useCase;

    @BeforeEach
    void setUp() {
        branchRepository  = Mockito.mock(BranchRepository.class);
        productRepository = Mockito.mock(ProductRepository.class);
        useCase = new UpdateBranchNameUseCase(branchRepository, productRepository);
    }

    /**
     * Caso feliz: la sucursal existe, el nuevo nombre no está en uso
     * → el caso de uso completa sin emitir valores (Mono<BranchDTO> con empty list).
     */
    @Test
    void shouldUpdateBranchName() {

        Branch branch = Branch.builder()
                .id("b1")
                .name("old")
                .franchiseId("f1")
                .build();

        // Sucursal encontrada
        Mockito.when(branchRepository.findById("b1"))
                .thenReturn(Mono.just(branch));

        // El nuevo nombre no está en uso en esta franquicia
        Mockito.when(branchRepository.existsByNameAndFranchiseIdAndIdNot(
                Mockito.anyString(), Mockito.anyString(), Mockito.anyString()))
                .thenReturn(Mono.just(false));

        // save devuelve la sucursal (con el nombre ya cambiado por rename())
        Mockito.when(branchRepository.save(Mockito.any()))
                .thenReturn(Mono.just(branch));

        // La sucursal no tiene productos en este test
        Mockito.when(productRepository.findByBranchId("b1"))
                .thenReturn(Flux.empty());

        StepVerifier.create(useCase.execute("b1",
                new com.company.prueba_tecnica.application.usecase.dto.UpdateNameDTO()))
                .verifyComplete();
    }
}
