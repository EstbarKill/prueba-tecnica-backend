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

/**
 * Tests unitarios para CreateProductUseCase.
 *
 * Verifica los tres escenarios posibles:
 *   1. Producto creado exitosamente
 *   2. Error cuando la sucursal no existe
 *   3. Error cuando el nombre del producto ya existe en la sucursal
 */
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

    /**
     * Caso feliz: sucursal existe, nombre disponible → producto creado.
     */
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

        // Sucursal existe, nombre libre, save exitoso
        Mockito.when(branchRepository.findById(branchId))
                .thenReturn(Mono.just(branch));

        Mockito.when(productRepository.existsByNameAndBranchId("Producto A", branchId))
                .thenReturn(Mono.just(false));

        Mockito.when(productRepository.save(Mockito.any()))
                .thenReturn(Mono.just(product));

        StepVerifier.create(useCase.execute("Producto A", 10, branchId))
                .expectNextMatches(p ->
                        p.getName().equals("Producto A") &&
                        p.getStock() == 10 &&
                        p.getBranchId().equals(branchId)
                )
                .verifyComplete();
    }

    /**
     * La sucursal no existe → debe emitir NotFoundException.
     */
    @Test
    void shouldFailWhenBranchNotFound() {

        // findById retorna Mono vacío → switchIfEmpty dispara NotFoundException
        Mockito.when(branchRepository.findById("invalid"))
                .thenReturn(Mono.empty());

        StepVerifier.create(useCase.execute("Producto A", 10, "invalid"))
                .expectErrorMatches(error ->
                        error instanceof NotFoundException &&
                        error.getMessage().equals("Branch not found")
                )
                .verify();
    }

    /**
     * Nombre de producto duplicado en la sucursal → DuplicateResourceException.
     */
    @Test
    void shouldFailWhenProductAlreadyExists() {

        String branchId = "branch-1";

        Branch branch = Branch.builder()
                .id(branchId)
                .name("Sucursal 1")
                .franchiseId("franchise-1")
                .build();

        // Sucursal existe, pero el nombre del producto ya está tomado
        Mockito.when(branchRepository.findById(branchId))
                .thenReturn(Mono.just(branch));

        Mockito.when(productRepository.existsByNameAndBranchId("Producto A", branchId))
                .thenReturn(Mono.just(true));

        StepVerifier.create(useCase.execute("Producto A", 10, branchId))
                .expectErrorMatches(error ->
                        error instanceof DuplicateResourceException &&
                        error.getMessage().equals("Product name already exists in this branch")
                )
                .verify();
    }
}
