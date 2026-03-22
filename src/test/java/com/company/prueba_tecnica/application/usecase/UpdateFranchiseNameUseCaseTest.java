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
 * Tests unitarios para UpdateFranchiseNameUseCase.
 *
 * Verifica que el nombre de una franquicia se actualiza correctamente
 * cuando no hay conflicto con otros nombres en el sistema.
 */
class UpdateFranchiseNameUseCaseTest {

    private FranchiseRepository franchiseRepository;
    private BranchRepository    branchRepository;
    private ProductRepository   productRepository;
    private UpdateFranchiseNameUseCase useCase;

    @BeforeEach
    void setUp() {
        franchiseRepository = Mockito.mock(FranchiseRepository.class);
        branchRepository    = Mockito.mock(BranchRepository.class);
        productRepository   = Mockito.mock(ProductRepository.class);

        useCase = new UpdateFranchiseNameUseCase(
                franchiseRepository,
                branchRepository,
                productRepository
        );
    }

    /**
     * Caso feliz: la franquicia existe, el nuevo nombre no está en uso
     * → actualización exitosa y el Mono completa.
     */
    @Test
    void shouldUpdateFranchise() {

        Franchise franchise = Franchise.builder()
                .id("f1")
                .name("old")
                .build();

        // Franquicia encontrada
        Mockito.when(franchiseRepository.findById("f1"))
                .thenReturn(Mono.just(franchise));

        // El nuevo nombre no está en uso por otra franquicia
        Mockito.when(franchiseRepository.existsByNameAndIdNot(
                Mockito.anyString(), Mockito.anyString()))
                .thenReturn(Mono.just(false));

        // save devuelve la franquicia actualizada
        Mockito.when(franchiseRepository.save(Mockito.any()))
                .thenReturn(Mono.just(franchise));

        // La franquicia no tiene sucursales en este test
        Mockito.when(branchRepository.findByFranchiseId("f1"))
                .thenReturn(Flux.empty());

        StepVerifier.create(useCase.execute("f1",
                new com.company.prueba_tecnica.application.usecase.dto.UpdateNameDTO()))
                .verifyComplete();
    }
}
