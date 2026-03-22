package com.company.prueba_tecnica.application.usecase;

import com.company.prueba_tecnica.domain.exception.DuplicateResourceException;
import com.company.prueba_tecnica.domain.exception.NotFoundException;
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

    private BranchRepository branchRepository;
    private ProductRepository productRepository;

    private CreateProductUseCase useCase;

    @BeforeEach
    void setUp() {
        branchRepository = Mockito.mock(BranchRepository.class);
        productRepository = Mockito.mock(ProductRepository.class);

        useCase = new CreateProductUseCase(branchRepository, productRepository);
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

        Mockito.when(branchRepository.findById(branchId))
                .thenReturn(Mono.just(branch));

        Mockito.when(productRepository.existsByNameAndBranchId("Producto A", branchId))
                .thenReturn(Mono.just(false));

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
    void shouldFailWhenBranchNotFound() {

        String branchId = "invalid";

        Mockito.when(branchRepository.findById(branchId))
                .thenReturn(Mono.empty());

        Mono<Product> result = useCase.execute("Producto A", 10, branchId);

        StepVerifier.create(result)
                .expectErrorMatches(error ->
                        error instanceof NotFoundException &&
                        error.getMessage().equals("Branch not found")
                )
                .verify();
    }

    @Test
    void shouldFailWhenProductAlreadyExists() {

        String branchId = "branch-1";

        Branch branch = Branch.builder()
                .id(branchId)
                .name("Sucursal 1")
                .franchiseId("franchise-1")
                .build();

        Mockito.when(branchRepository.findById(branchId))
                .thenReturn(Mono.just(branch));

        Mockito.when(productRepository.existsByNameAndBranchId("Producto A", branchId))
                .thenReturn(Mono.just(true));

        Mono<Product> result = useCase.execute("Producto A", 10, branchId);

        StepVerifier.create(result)
                .expectErrorMatches(error ->
                        error instanceof DuplicateResourceException &&
                        error.getMessage().equals("Product name already exists in this branch")
                )
                .verify();
    }
}