package com.company.prueba_tecnica.application.usecase;

import com.company.prueba_tecnica.domain.model.*;
import com.company.prueba_tecnica.domain.repository.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

/**
 * Tests unitarios para GetFranchiseStructureUseCase.
 *
 * Verifica que el caso de uso construye correctamente la estructura
 * jerárquica Franquicia → Sucursal → Productos a partir de los repositorios.
 */
class GetFranchiseStructureUseCaseTest {

    private FranchiseRepository franchiseRepository;
    private BranchRepository branchRepository;
    private ProductRepository productRepository;
    private GetFranchiseStructureUseCase useCase;

    @BeforeEach
    void setUp() {
        franchiseRepository = Mockito.mock(FranchiseRepository.class);
        branchRepository    = Mockito.mock(BranchRepository.class);
        productRepository   = Mockito.mock(ProductRepository.class);

        useCase = new GetFranchiseStructureUseCase(
                franchiseRepository,
                branchRepository,
                productRepository
        );
    }

    /**
     * Con una franquicia, una sucursal y un producto configurados,
     * el Flux debe emitir exactamente un FranchiseDTO completo.
     */
    @Test
    void shouldReturnFranchiseStructure() {

        Franchise franchise = Franchise.builder().id("f1").name("Franchise").build();
        Branch    branch    = Branch.builder().id("b1").name("Branch").franchiseId("f1").build();
        Product   product   = Product.builder().id("p1").name("Prod").stock(10).branchId("b1").build();

        // Configurar mocks para simular la jerarquía completa
        Mockito.when(franchiseRepository.findAll()).thenReturn(Flux.just(franchise));
        Mockito.when(branchRepository.findByFranchiseId("f1")).thenReturn(Flux.just(branch));
        Mockito.when(productRepository.findByBranchId("b1")).thenReturn(Flux.just(product));

        // Verificar que se emite exactamente 1 FranchiseDTO y el stream completa
        StepVerifier.create(useCase.execute())
                .expectNextCount(1)
                .verifyComplete();
    }
}
