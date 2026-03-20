package com.company.prueba_tecnica.application.usecase;

import com.company.prueba_tecnica.domain.model.Branch;
import com.company.prueba_tecnica.domain.model.Product;
import com.company.prueba_tecnica.domain.repository.BranchRepository;
import com.company.prueba_tecnica.domain.repository.ProductRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

class CreateProductUseCaseTest {

    private ProductRepository productRepository;
    private BranchRepository branchRepository;

    private CreateProductUseCase useCase;

    @BeforeEach
    void setUp() {
        productRepository = Mockito.mock(ProductRepository.class);
        branchRepository = Mockito.mock(BranchRepository.class);

        useCase = new CreateProductUseCase(productRepository, branchRepository);
    }

    @Test
    void shouldCreateProductSuccessfully() {

        String branchId = "branch-1";

        Branch branch = Branch.builder()
                .id(branchId)
                .name("Sucursal 1")
                .franchiseId("franchise-1")
                .build();

        Product product = Product.builder()
                .id("prod-1")
                .name("Producto A")
                .stock(10)
                .branchId(branchId)
                .build();

        // Mock comportamiento
        Mockito.when(branchRepository.findById(branchId))
                .thenReturn(Mono.just(branch));

        Mockito.when(productRepository.save(Mockito.any()))
                .thenReturn(Mono.just(product));

        Mono<Product> result = useCase.execute("Producto A", 10, branchId);

        StepVerifier.create(result)
                .expectNextMatches(p ->
                        p.getName().equals("Producto A") &&
                        p.getStock() == 10 &&
                        p.getBranchId().equals(branchId)
                )
                .verifyComplete();
    }

    @Test
    void shouldFailWhenBranchNotExists() {

        String branchId = "invalid";

        Mockito.when(branchRepository.findById(branchId))
                .thenReturn(Mono.empty());

        Mono<Product> result = useCase.execute("Producto A", 10, branchId);

        StepVerifier.create(result)
                .expectErrorMatches(error ->
                        error instanceof RuntimeException &&
                        error.getMessage().equals("Branch not found")
                )
                .verify();
    }
}