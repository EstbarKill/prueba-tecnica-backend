package com.company.prueba_tecnica.application.usecase;

import com.company.prueba_tecnica.domain.model.Product;
import com.company.prueba_tecnica.domain.repository.ProductRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

class UpdateProductNameUseCaseTest {

    private ProductRepository productRepository;
    private UpdateProductNameUseCase useCase;

    @BeforeEach
    void setUp() {
        productRepository = Mockito.mock(ProductRepository.class);
        useCase = new UpdateProductNameUseCase(productRepository);
    }

    @Test
    void shouldUpdateProductName() {

        Product product = Product.builder()
                .id("p1")
                .name("old")
                .stock(10)
                .branchId("b1")
                .build();

        Mockito.when(productRepository.findById("p1"))
                .thenReturn(Mono.just(product));

        Mockito.when(productRepository.existsByNameAndBranchIdAndIdNot(
                Mockito.anyString(), Mockito.anyString(), Mockito.anyString()))
                .thenReturn(Mono.just(false));

        Mockito.when(productRepository.save(Mockito.any()))
                .thenReturn(Mono.just(product));

        StepVerifier.create(useCase.execute("p1", new com.company.prueba_tecnica.application.usecase.dto.UpdateNameDTO()))
                .verifyComplete();
    }
}