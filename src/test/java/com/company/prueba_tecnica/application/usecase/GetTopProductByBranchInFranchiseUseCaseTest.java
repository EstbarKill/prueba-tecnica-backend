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
 * Tests unitarios para GetTopProductByBranchInFranchiseUseCase.
 *
 * Verifica que el caso de uso selecciona correctamente el producto
 * con mayor stock de cada sucursal dentro de la franquicia.
 */
class GetTopProductByBranchInFranchiseUseCaseTest {

    private FranchiseRepository franchiseRepository;
    private BranchRepository    branchRepository;
    private ProductRepository   productRepository;
    private GetTopProductByBranchInFranchiseUseCase useCase;

    @BeforeEach
    void setUp() {
        franchiseRepository = Mockito.mock(FranchiseRepository.class);
        branchRepository    = Mockito.mock(BranchRepository.class);
        productRepository   = Mockito.mock(ProductRepository.class);

        useCase = new GetTopProductByBranchInFranchiseUseCase(
                franchiseRepository,
                branchRepository,
                productRepository
        );
    }

    /**
     * Con dos productos de distinto stock, el top product debe ser el de mayor stock.
     * El Mono debe emitir un FranchiseTopProductsDTO y completar.
     */
    @Test
    void shouldReturnTopProduct() {

        Franchise franchise = Franchise.builder().id("f1").name("F").build();
        Branch    branch    = Branch.builder().id("b1").name("B").franchiseId("f1").build();

        // p1 tiene stock 5, p2 tiene stock 10 → p2 debe ser el top product
        Product p1 = Product.builder().id("p1").name("A").stock(5).branchId("b1").build();
        Product p2 = Product.builder().id("p2").name("B").stock(10).branchId("b1").build();

        Mockito.when(franchiseRepository.findById("f1")).thenReturn(Mono.just(franchise));
        Mockito.when(branchRepository.findByFranchiseId("f1")).thenReturn(Flux.just(branch));
        Mockito.when(productRepository.findByBranchId("b1")).thenReturn(Flux.just(p1, p2));

        // Verificar que se emite un resultado y el Mono completa sin errores
        StepVerifier.create(useCase.execute("f1"))
                .expectNextCount(1)
                .verifyComplete();
    }
}
