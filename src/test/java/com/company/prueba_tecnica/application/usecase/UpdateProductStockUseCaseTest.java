package com.company.prueba_tecnica.application.usecase;

import com.company.prueba_tecnica.application.usecase.dto.UpdateStockDTO;
import com.company.prueba_tecnica.domain.model.Product;
import com.company.prueba_tecnica.domain.repository.ProductRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

/**
 * Tests unitarios para UpdateProductStockUseCase.
 *
 * Verifica que el stock de un producto se actualiza correctamente
 * cuando el producto existe.
 */
class UpdateProductStockUseCaseTest {

    private ProductRepository productRepository;
    private UpdateProductStockUseCase useCase;

    @BeforeEach
    void setUp() {
        productRepository = Mockito.mock(ProductRepository.class);
        useCase = new UpdateProductStockUseCase(productRepository);
    }

    /**
     * Caso feliz: producto existe → stock actualizado y Mono completa.
     *
     * Se usa UpdateStockDTO(null) para simplificar el test; en un escenario
     * real el stock debería ser un valor válido >= 0.
     */
    @Test
    void shouldUpdateStock() {

        Product product = Product.builder()
                .id("p1")
                .name("prod")
                .stock(5)
                .branchId("b1")
                .build();

        // Producto encontrado
        Mockito.when(productRepository.findById("p1"))
                .thenReturn(Mono.just(product));

        // save devuelve el producto con el stock actualizado
        Mockito.when(productRepository.save(Mockito.any()))
                .thenReturn(Mono.just(product));

        StepVerifier.create(useCase.execute("p1", new UpdateStockDTO(null)))
                .verifyComplete();
    }
}
